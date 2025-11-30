package vn.socialnet.service;

import vn.socialnet.dto.response.UserSummaryResponse;
import vn.socialnet.model.User;

import java.util.List;

public interface FollowService {

    int totalFollowers(User follower);

    int totalFollowings(User follower);

    boolean isFollowing(Long followerId, Long followeeId);


}
