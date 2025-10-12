package vn.socialnet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import vn.socialnet.model.Follow;
import vn.socialnet.model.User;

import java.util.List;


@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {


    List<User> findFolloweesByFollower(User follower);
}
