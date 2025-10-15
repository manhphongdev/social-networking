package vn.socialnet.service;

import vn.socialnet.model.User;

public interface FollowService {

    int totalFollowers(User follower);

    int totalFollowings(User follower);
}
