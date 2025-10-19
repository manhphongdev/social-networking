package vn.socialnet.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vn.socialnet.dto.response.ResponseData;
import vn.socialnet.dto.response.UserSummaryResponse;
import vn.socialnet.model.User;
import vn.socialnet.service.FollowService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    /**
     * POST /follow/{userId} - Follow a user
     */
    @PostMapping("/{userId}")
    public ResponseData<Void> followUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal User user) {
        log.info("User {} following user {}", user.getId(), userId);
        followService.followUser(user.getId(), userId);
        return new ResponseData<>(HttpStatus.OK.value(), "Successfully followed user", null);
    }

    /**
     * DELETE /follow/{userId} - Unfollow a user
     */
    @DeleteMapping("/{userId}")
    public ResponseData<Void> unfollowUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal User user) {
        log.info("User {} unfollowing user {}", user.getId(), userId);
        followService.unfollowUser(user.getId(), userId);
        return new ResponseData<>(HttpStatus.OK.value(), "Successfully unfollowed user", null);
    }

    /**
     * GET /follow/suggestions - Get suggested users to follow
     */
    @GetMapping("/suggestions")
    public ResponseData<List<UserSummaryResponse>> getSuggestions(
            @RequestParam(defaultValue = "10") int limit,
            @AuthenticationPrincipal User user) {
        log.info("Get follow suggestions for user {}", user.getId());
        List<UserSummaryResponse> suggestions = followService.getSuggestedUsers(user.getId(), limit);
        return new ResponseData<>(HttpStatus.OK.value(), "Suggestions retrieved successfully", suggestions);
    }
}
