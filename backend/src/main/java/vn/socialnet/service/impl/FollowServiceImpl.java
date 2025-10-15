package vn.socialnet.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.socialnet.model.User;
import vn.socialnet.repository.FollowRepository;
import vn.socialnet.service.FollowService;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;

    @Override
    public int totalFollowers(User follower) {
        return followRepository.countFollowsByFollowee(follower);
    }

    @Override
    public int totalFollowings(User follower) {
        return followRepository.countFollowsByFollower(follower);
    }
}
