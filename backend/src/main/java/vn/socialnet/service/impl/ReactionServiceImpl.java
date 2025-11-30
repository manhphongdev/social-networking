package vn.socialnet.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.socialnet.dto.response.UserSummaryResponse;
import vn.socialnet.enums.ReactionType;
import vn.socialnet.exception.ResourceNotFoundException;
import vn.socialnet.model.Post;
import vn.socialnet.model.Reaction;
import vn.socialnet.model.ReactionId;
import vn.socialnet.model.User;
import vn.socialnet.repository.PostRepository;
import vn.socialnet.repository.ReactionRepository;
import vn.socialnet.repository.UserRepository;
import vn.socialnet.service.FollowService;
import vn.socialnet.service.NotificationService;
import vn.socialnet.service.ReactionService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FollowService followService;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public void likePost(Long postId, Long userId) {
        User user = getUserById(userId);
        Post post = getPostById(postId);

        // Check if already liked
        if (reactionRepository.findByUserAndPost(user, post).isPresent()) {
            throw new IllegalStateException("Already liked this post");
        }

        Reaction reaction = Reaction.builder()
                .reactionId(new ReactionId(userId, postId))
                .user(user)
                .post(post)
                .type(ReactionType.LIKE) // Default to LIKE
                .build();

        reactionRepository.save(reaction);
        log.info("User {} liked post {}", userId, postId);

        if (!post.getUser().getId().equals(userId)) {
            notificationService.createNotification(
                    post.getUser(),
                    user,
                    vn.socialnet.enums.NotificationType.LIKE,
                    vn.socialnet.enums.NotificationTargetType.POST,
                    post.getId(),
                    user.getName() + " liked your post."
            );
        }
    }

    @Override
    @Transactional
    public void unlikePost(Long postId, Long userId) {
        User user = getUserById(userId);
        Post post = getPostById(postId);

        Reaction reaction = reactionRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new ResourceNotFoundException("Reaction not found"));

        reactionRepository.delete(reaction);
        log.info("User {} unliked post {}", userId, postId);
    }

    @Override
    public boolean hasLiked(Long postId, Long userId) {
        User user = getUserById(userId);
        Post post = getPostById(postId);
        return reactionRepository.findByUserAndPost(user, post).isPresent();
    }

    @Override
    public List<UserSummaryResponse> getPostLikes(Long postId, Long currentUserId) {
        // Verify post exists
        getPostById(postId);

        List<User> users = reactionRepository.findUsersByPostId(postId);

        return users.stream()
                .map(user -> mapToUserSummaryResponse(user, currentUserId))
                .collect(Collectors.toList());
    }

    private UserSummaryResponse mapToUserSummaryResponse(User user, Long currentUserId) {
        return UserSummaryResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .avatar(user.getAvatar())
                .bio(user.getBio())
                .isFollowing(followService.isFollowing(currentUserId, user.getId()))
                .followersCount(followService.totalFollowers(user))
                .followingCount(followService.totalFollowings(user))
                .build();
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
    }
}
