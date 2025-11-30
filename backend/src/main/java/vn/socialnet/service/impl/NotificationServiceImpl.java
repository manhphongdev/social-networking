package vn.socialnet.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.socialnet.dto.response.NotificationResponse;
import vn.socialnet.enums.NotificationTargetType;
import vn.socialnet.enums.NotificationType;
import vn.socialnet.model.Notification;
import vn.socialnet.model.User;
import vn.socialnet.repository.NotificationRepository;
import vn.socialnet.service.NotificationService;


@Slf4j
@Service
@RequiredArgsConstructor

public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;


    @Override
    @Transactional
    public void createNotification(User toUser, User fromUser,
                                   NotificationType type,
                                   NotificationTargetType targetType,
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
        simpMessagingTemplate.convertAndSendToUser(
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
}
