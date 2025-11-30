package vn.socialnet.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.socialnet.model.Post;
import vn.socialnet.model.User;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    // Find feed posts from followed users
    @Query("SELECT DISTINCT p FROM posts p " +
            "WHERE p.user.id IN :followedUserIds " +
            "AND (p.privacy = 'PUBLIC' OR p.privacy = 'FRIENDS') " +
            "ORDER BY p.createdAt DESC")
    Page<Post> findFeedPosts(@Param("followedUserIds") List<Long> followedUserIds, Pageable pageable);


    // Find trending posts with engagement score
    // Score = (reactions count * 2) + (comments count * 3) + recency bonus
    @Query("SELECT p FROM posts p " +
            "LEFT JOIN p.reactions r " +
            "LEFT JOIN p.comments c " +
            "WHERE p.privacy = 'PUBLIC' " +
            "GROUP BY p.id " +
            "ORDER BY (COUNT(DISTINCT r.reactionId) * 2 + COUNT(DISTINCT c.id) * 3) DESC, p.createdAt DESC")
    Page<Post> findTrendingPosts(Pageable pageable);
}
