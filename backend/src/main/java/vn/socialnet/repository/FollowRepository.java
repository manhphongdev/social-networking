package vn.socialnet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.socialnet.model.Follow;
import vn.socialnet.model.User;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    int countFollowsByFollowee(User followee);

    int countFollowsByFollower(User follower);
}
