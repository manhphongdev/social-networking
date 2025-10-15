package vn.socialnet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.socialnet.model.PostMedia;

@Repository
public interface PostMediaRepository extends JpaRepository<PostMedia, Long> {
}
