package vn.socialnet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.socialnet.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
