package vn.socialnet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.socialnet.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
