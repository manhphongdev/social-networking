package vn.socialnet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.socialnet.model.Post;
import vn.socialnet.model.Reaction;
import vn.socialnet.model.ReactionId;
import vn.socialnet.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, ReactionId> {

    /**
     * Find reaction by user and post
     */
    Optional<Reaction> findByUserAndPost(User user, Post post);

    /**
     * Get all users who reacted to a post
     */
    @Query("SELECT r.user FROM Reactions r WHERE r.post.id = :postId")
    List<User> findUsersByPostId(@Param("postId") Long postId);

    /**
     * Count reactions for a post
     */
    Long countByPost(Post post);
}
