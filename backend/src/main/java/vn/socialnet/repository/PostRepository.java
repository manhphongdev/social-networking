package vn.socialnet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.socialnet.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
