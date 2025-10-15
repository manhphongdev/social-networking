package vn.socialnet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.socialnet.model.Post;
import vn.socialnet.model.User;

public interface PostRepository extends JpaRepository<Post, Long> {
    int countByUser(User user);
}
