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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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


    @Override
    public boolean isFollowing(Long followerId, Long followeeId) {
        return followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId).isPresent();
    }
}
