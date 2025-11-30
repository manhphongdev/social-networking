package vn.socialnet.service;

import vn.socialnet.dto.response.UserSummaryResponse;

import java.util.List;

public interface ReactionService {

    /**
     * Like a post
     */
    void likePost(Long postId, Long userId);

    /**
     * Unlike a post
     */
    void unlikePost(Long postId, Long userId);

    /**
     * Check if user liked a post
     */
    boolean hasLiked(Long postId, Long userId);

    /**
     * Get list of users who liked a post
     */
    List<UserSummaryResponse> getPostLikes(Long postId, Long currentUserId);
}
