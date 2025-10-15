package vn.socialnet.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.socialnet.dto.request.EditPostRequest;
import vn.socialnet.dto.request.PostCreationRequest;
import vn.socialnet.enums.MediaType;
import vn.socialnet.enums.PostPrivates;
import vn.socialnet.exception.AppException;
import vn.socialnet.exception.ErrorCode;
import vn.socialnet.exception.ResourceNotFoundException;
import vn.socialnet.model.Post;
import vn.socialnet.model.PostMedia;
import vn.socialnet.model.User;
import vn.socialnet.repository.PostMediaRepository;
import vn.socialnet.repository.PostRepository;
import vn.socialnet.repository.UserRepository;
import vn.socialnet.service.PostService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMediaRepository postMediaRepository;
    private final S3Service s3Service;

    @Override
    public int totalPosts(User user) {
        return postRepository.countByUser(user);
    }

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

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Post getPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
    }
}
