package vn.socialnet.service;

import vn.socialnet.dto.response.NotificationResponse;
import vn.socialnet.enums.NotificationTargetType;
import vn.socialnet.enums.NotificationType;
import vn.socialnet.model.User;

import java.util.List;

public interface NotificationService {

    /**
     * Get all notifications for a user
     */
    List<NotificationResponse> getNotifications(Long userId);

    /**
     * Get unread notification count for a user
     */
    Long getUnreadCount(Long userId);

    /**
     * Mark a notification as read
     */
    void markAsRead(Long notificationId, Long userId);

    /**
     * Mark all notifications as read for a user
     */
    /**
     * Mark all notifications as read for a user
     */
    void markAllAsRead(Long userId);

    /**
     * Create a new notification and push to user
     */
    void createNotification(User toUser, User fromUser,
                           NotificationType type,
                            NotificationTargetType targetType,
                            Long targetId, String text);
}
