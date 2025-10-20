package vn.socialnet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.socialnet.model.Follow;
import vn.socialnet.model.User;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    int countFollowsByFollowee(User followee);

    int countFollowsByFollower(User follower);

    Optional<Follow> findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    // Get suggested users (users not followed by current user, ordered by followers count)
    @Query("SELECT u FROM User u WHERE u.id != :userId " +
            "AND u.id NOT IN (SELECT f.followee.id FROM follows f WHERE f.follower.id = :userId) " +
            "ORDER BY (SELECT COUNT(f2) FROM follows f2 WHERE f2.followee.id = u.id) DESC")
    List<User> findSuggestedUsers(@Param("userId") Long userId);
}
