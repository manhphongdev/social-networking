package vn.socialnet.service;

public interface NotificationService {
    /**
     * Create a new notification and push to user
     */
    void createNotification(vn.socialnet.model.User toUser, vn.socialnet.model.User fromUser,
                            vn.socialnet.enums.NotificationType type,
                            vn.socialnet.enums.NotificationTargetType targetType,
                            Long targetId, String text);

}
