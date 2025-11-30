package vn.socialnet.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.socialnet.dto.request.EditPostRequest;
import vn.socialnet.dto.request.PostCreationRequest;
import vn.socialnet.dto.response.PageResponse;
import vn.socialnet.dto.response.PostMediaResponse;
import vn.socialnet.dto.response.PostResponse;
import vn.socialnet.enums.MediaType;
import vn.socialnet.enums.PostPrivates;
import vn.socialnet.exception.AppException;
import vn.socialnet.exception.ErrorCode;
import vn.socialnet.exception.ResourceNotFoundException;
import vn.socialnet.model.Post;
import vn.socialnet.model.PostMedia;
import vn.socialnet.model.Reaction;
import vn.socialnet.model.User;
import vn.socialnet.repository.FollowRepository;
import vn.socialnet.repository.PostMediaRepository;
import vn.socialnet.repository.PostRepository;
import vn.socialnet.repository.UserRepository;
import vn.socialnet.service.PostService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final PostMediaRepository postMediaRepository;


    @Transactional
    @Override
    public Long addPost(PostCreationRequest request) {
        //add file
        List<MultipartFile> files = request.getFile();
        List<String> urls = new ArrayList<>();
        String url;

        User user = getUserById(request.getUserId());

        //add Post
        Post post = Post.builder()
                .caption(request.getCaption())
                .user(user)
                .privacy(request.getPrivacy())
                .build();

        List<PostMedia> postMedias = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                url = s3Service.uploadFile(file);
                urls.add(url);
                postMedias.add(PostMedia.builder()
                        .url(url)
                        .post(post)
                        .mediaType(getMediaType(file))
                        .build());
            } catch (IOException e) {
                throw new AppException(ErrorCode.FILE_UPLOAD_FIELD);
            }
        }

        post.setMedia(postMedias);
        postRepository.save(post);
        return post.getId();
    }

    @Override
    public void updatePost(Long postId, Long userId, EditPostRequest request) {

        //check Post exist
        Post post = getPostById(postId);

        //check permission
        if (!post.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to edit this post");
        }

        //do update
        if (StringUtils.hasText(request.getCaption())) {
            post.setCaption(request.getCaption());
        }

        if (StringUtils.hasText(request.getPrivacy())) {
            post.setPrivacy(PostPrivates.valueOf(request.getPrivacy().toUpperCase()));
        }

        List<PostMedia> postMedias = post.getMedia();

        //delete old media if has request
        if (request.getRemovedMediaIds() != null) {
            for (PostMedia postMedia : postMedias) {
                if (postMedia.getId().equals(request.getRemovedMediaIds())) {
                    postMediaRepository.delete(postMedia);
                    s3Service.deleteFile(postMedia.getUrl());
                    log.info("Removed post media with id " + request.getRemovedMediaIds());
                }
            }
        }

        //add new media
        List<MultipartFile> files = request.getFile();

        if (files != null) {
            for (MultipartFile file : files) {
                try {
                    String mediaUrl = s3Service.uploadFile(file);
                    PostMedia postMedia = PostMedia.builder()
                            .mediaType(getMediaType(file))
                            .post(post)
                            .url(mediaUrl)
                            .build();
                    postMedias.add(postMedia);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        post.setMedia(postMedias);
        postRepository.save(post);
    }

    @Override
    public PostResponse getPostById(Long postId, Long currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        // Check privacy permissions
        if (!canViewPost(post, currentUserId)) {
            throw new AccessDeniedException("You do not have permission to view this post");
        }

        return mapToPostResponse(post, currentUserId);
    }

    @Transactional
    @Override
    public void deletePost(Long postId, Long userId) {
        Post post = getPostById(postId);

        // Check ownership
        if (!post.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to delete this post");
        }

        // Delete associated media files from S3
        for (PostMedia media : post.getMedia()) {
            s3Service.deleteFile(media.getUrl());
        }

        // Delete the post (cascades will handle related entities)
        postRepository.delete(post);
        log.info("Deleted post with id {} by user {}", postId, userId);
    }

    @Override
    public PageResponse<?> getUserPosts(Long userId, Long currentUserId, int page, int size) {
        User user = getUserById(userId);

        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = postRepository.findByUserOrderByCreatedAtDesc(user, pageable);

        // Filter based on privacy
        List<PostResponse> postResponses = postPage.getContent().stream()
                .filter(post -> canViewPost(post, currentUserId))
                .map(post -> mapToPostResponse(post, currentUserId))
                .collect(Collectors.toList());

        return PageResponse.builder()
                .pageNo(page)
                .pageSize(size)
                .totalPages(postPage.getTotalPages())
                .items(postResponses)
                .build();
    }

    @Override
    public PageResponse<?> getFeed(Long userId, int page, int size) {
        User user = getUserById(userId);

        // Get list of users that the current user follows
        List<User> followedUsers = followRepository.findFolloweesByFollower(user);

        if (followedUsers.isEmpty()) {
            // Return empty feed if not following anyone
            return PageResponse.builder()
                    .pageNo(page)
                    .pageSize(size)
                    .totalPages(0)
                    .items(followedUsers)
                    .build();
        }

        // Get IDs of followed users
        List<Long> followedUserIds = followedUsers.stream()
                .map(User::getId)
                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = postRepository.findFeedPosts(followedUserIds, pageable);

        List<PostResponse> postResponses = postPage.getContent().stream()
                .map(post -> mapToPostResponse(post, userId))
                .collect(Collectors.toList());

        return PageResponse.builder()
                .pageNo(page)
                .pageSize(size)
                .totalPages(postPage.getTotalPages())
                .items(postResponses)
                .build();
    }

    @Override
    public PageResponse<?> getExploreFeed(Long currentUserId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = postRepository.findTrendingPosts(pageable);

        List<PostResponse> postResponses = postPage.getContent().stream()
                .map(post -> mapToPostResponse(post, currentUserId))
                .collect(Collectors.toList());

        return PageResponse.builder()
                .pageNo(page)
                .pageSize(size)
                .totalPages(postPage.getTotalPages())
                .items(postResponses)
                .build();
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private MediaType getMediaType(MultipartFile file) {
        Tika tika = new Tika();
        try {
            String detectedType = tika.detect(file.getInputStream());
            if (detectedType.startsWith("image/")) {
                return MediaType.IMAGE;
            } else if (detectedType.startsWith("video/")) {
                return MediaType.VIDEO;
            } else {
                throw new AppException(ErrorCode.UNSUPPORTED_TYPE_OF_FILE);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    /**
     * Map Post entity to PostResponse DTO
     */
    private PostResponse mapToPostResponse(Post post, Long currentUserId) {
        User postUser = post.getUser();

        // Map media
        List<PostMediaResponse> mediaResponses = post.getMedia().stream()
                .map(media -> PostMediaResponse.builder()
                        .id(media.getId())
                        .url(media.getUrl())
                        .mediaType(media.getMediaType())
                        .build())
                .collect(Collectors.toList());

        // Calculate reaction counts by type
        Map<String, Long> reactionCounts = post.getReactions().stream()
                .collect(Collectors.groupingBy(
                        reaction -> reaction.getType().name(),
                        Collectors.counting()
                ));

        long totalReactions = post.getReactions().size();

        // Check if current user has reacted and get reaction type
        boolean hasReacted = false;
        String userReactionType = null;
        if (currentUserId != null) {
            for (Reaction reaction : post.getReactions()) {
                if (reaction.getUser().getId().equals(currentUserId)) {
                    hasReacted = true;
                    userReactionType = reaction.getType().name();
                    break;
                }
            }
        }

        return PostResponse.builder()
                .id(post.getId())
                .caption(post.getCaption())
                .privacy(post.getPrivacy())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .userId(postUser.getId())
                .userName(postUser.getName())
                .userAvatar(postUser.getAvatar())
                .media(mediaResponses)
                .reactionCounts(reactionCounts)
                .totalReactions(totalReactions)
                .commentCount((long) post.getComments().size())
                .isOwner(postUser.getId().equals(currentUserId))
                .hasReacted(hasReacted)
                .userReactionType(userReactionType)
                .build();
    }
    /**
     * Check if a user can view a post based on privacy settings
     */
    private boolean canViewPost(Post post, Long currentUserId) {
        // Owner can always view their own posts
        if (post.getUser().getId().equals(currentUserId)) {
            return true;
        }

        // Anyone can view public posts
        if (post.getPrivacy() == PostPrivates.PUBLIC) {
            return true;
        }

        // For FRIENDS privacy, check if current user follows the post owner
        if (post.getPrivacy() == PostPrivates.FRIENDS) {
            User currentUser = getUserById(currentUserId);
            List<User> followedUsers = followRepository.findFolloweesByFollower(currentUser);
            return followedUsers.stream()
                    .anyMatch(followedUser -> followedUser.getId().equals(post.getUser().getId()));
        }

        // PRIVATE posts are only visible to owner (already checked above)
        return false;
    }

    private Post getPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
    }

}
