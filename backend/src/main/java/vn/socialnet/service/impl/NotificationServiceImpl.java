package vn.socialnet.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.socialnet.dto.response.NotificationResponse;
import vn.socialnet.exception.ResourceNotFoundException;
import vn.socialnet.model.Notification;
import vn.socialnet.model.User;
import vn.socialnet.repository.NotificationRepository;
import vn.socialnet.repository.UserRepository;
import vn.socialnet.service.NotificationService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public List<NotificationResponse> getNotifications(Long userId) {
        User user = getUserById(userId);
        List<Notification> notifications = notificationRepository.findByUserOrderByCreatedAtDesc(user);

        return notifications.stream()
                .map(this::mapToNotificationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReaddedFalse(userId);
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        notificationRepository.markAsRead(notificationId, userId);
        log.info("Marked notification {} as read for user {}", notificationId, userId);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsRead(userId);
        log.info("Marked all notifications as read for user {}", userId);
    }

    @Override
    @Transactional
    public void createNotification(vn.socialnet.model.User toUser, vn.socialnet.model.User fromUser,
                                   vn.socialnet.enums.NotificationType type,
                                   vn.socialnet.enums.NotificationTargetType targetType,
                                   Long targetId, String text) {
        Notification notification = Notification.builder()
                .user(toUser)
                .fromUser(fromUser)
                .type(type)
                .targetType(targetType)
                .targetId(targetId)
                .text(text)
                .isReadded(false)
                .build();

        notification = notificationRepository.save(notification);

        NotificationResponse response = mapToNotificationResponse(notification);
        messagingTemplate.convertAndSendToUser(
                toUser.getUsername(),
                "/queue/notifications",
                response
        );
        log.info("Created and pushed notification to user {}", toUser.getUsername());
    }

    /**
     * Helper method to map Notification entity to NotificationResponse DTO
     */
    private NotificationResponse mapToNotificationResponse(Notification notification) {
        User fromUser = notification.getFromUser();

        return NotificationResponse.builder()
                .id(notification.getId())
                .userId(notification.getUser().getId())
                .fromUserId(fromUser.getId())
                .fromUserName(fromUser.getName())
                .fromUserAvatar(fromUser.getAvatar())
                .type(notification.getType())
                .targetType(notification.getTargetType())
                .targetId(notification.getTargetId())
                .text(notification.getText())
                .isReadded(notification.isReadded())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
