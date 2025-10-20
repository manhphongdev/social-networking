package vn.socialnet.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.socialnet.dto.response.UserSummaryResponse;
import vn.socialnet.exception.ResourceNotFoundException;
import vn.socialnet.model.Follow;
import vn.socialnet.model.FollowId;
import vn.socialnet.model.User;
import vn.socialnet.repository.FollowRepository;
import vn.socialnet.repository.UserRepository;
import vn.socialnet.service.FollowService;
import vn.socialnet.service.NotificationService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Override
    public int totalFollowers(User follower) {
        return followRepository.countFollowsByFollowee(follower);
    }

    @Override
    public int totalFollowings(User follower) {
        return followRepository.countFollowsByFollower(follower);
    }

    @Override
    @Transactional
    public void followUser(Long followerId, Long followeeId) {
        if (followerId.equals(followeeId)) {
            throw new IllegalArgumentException("Cannot follow yourself");
        }

        User follower = getUserById(followerId);
        User followee = getUserById(followeeId);

        // Check if already following
        if (followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId).isPresent()) {
            throw new IllegalStateException("Already following this user");
        }

        Follow follow = Follow.builder()
                .followId(new FollowId(followerId, followeeId))
                .follower(follower)
                .followee(followee)
                .createdAt(LocalDateTime.now())
                .build();

        followRepository.save(follow);
        log.info("User {} followed user {}", followerId, followeeId);

        notificationService.createNotification(
                followee,
                follower,
                vn.socialnet.enums.NotificationType.FOLLOW,
                vn.socialnet.enums.NotificationTargetType.USER,
                followerId,
                follower.getName() + " started following you."
        );
    }

    @Override
    @Transactional
    public void unfollowUser(Long followerId, Long followeeId) {
        Follow follow = followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Follow relationship not found"));

        followRepository.delete(follow);
        log.info("User {} unfollowed user {}", followerId, followeeId);
    }

    @Override
    public boolean isFollowing(Long followerId, Long followeeId) {
        return followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId).isPresent();
    }

    /**
     * Returns a list of suggested users that the current user may want to follow.
     *
     * Workflow:
     * 1. Fetch raw suggested users from the repository (already filtered by query logic:
     *    - exclude the current user
     *    - exclude users the current user already follows).
     * 2. Limit the result size based on the provided "limit" value.
     * 3. Convert each User entity into UserSummaryResponse (DTO) for FE rendering.
     *
     * @param userId the ID of the current authenticated user
     * @param limit the maximum number of users to return
     * @return a list of user summary DTOs representing suggested users
     */

    @Override
    public List<UserSummaryResponse> getSuggestedUsers(Long userId, int limit) {
        List<User> suggestedUsers = followRepository.findSuggestedUsers(userId);

        return suggestedUsers.stream()
                .limit(limit)
                .map(user -> mapToUserSummaryResponse(user, userId))
                .collect(Collectors.toList());
    }

    private UserSummaryResponse mapToUserSummaryResponse(User user, Long currentUserId) {
        return UserSummaryResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .avatar(user.getAvatar())
                .bio(user.getBio())
                .isFollowing(isFollowing(currentUserId, user.getId()))
                .followersCount(totalFollowers(user))
                .followingCount(totalFollowings(user))
                .build();
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
