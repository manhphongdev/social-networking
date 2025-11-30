package vn.socialnet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import vn.socialnet.model.Follow;
import vn.socialnet.model.User;

import java.util.List;
import java.util.Optional;


@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {


    List<User> findFolloweesByFollower(User follower);

    Optional<Object> findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    int countFollowsByFollowee(User followee);

    int countFollowsByFollower(User follower);
}
