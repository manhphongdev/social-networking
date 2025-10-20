package vn.socialnet.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vn.socialnet.dto.response.NotificationResponse;
import vn.socialnet.dto.response.ResponseData;
import vn.socialnet.model.User;
import vn.socialnet.service.NotificationService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * GET /notifications - Get all notifications for current user
     */
    @GetMapping
    public ResponseData<List<NotificationResponse>> getNotifications(@AuthenticationPrincipal User user) {
        log.info("Get notifications for user {}", user.getId());
        List<NotificationResponse> notifications = notificationService.getNotifications(user.getId());
        return new ResponseData<>(HttpStatus.OK.value(), "Notifications retrieved successfully", notifications);
    }

    /**
     * GET /notifications/unread-count - Get unread notification count
     */
    @GetMapping("/unread-count")
    public ResponseData<Long> getUnreadCount(@AuthenticationPrincipal User user) {
        log.info("Get unread count for user {}", user.getId());
        Long count = notificationService.getUnreadCount(user.getId());
        return new ResponseData<>(HttpStatus.OK.value(), "Unread count retrieved", count);
    }

    /**
     * PUT /notifications/{id}/read - Mark notification as read
     */
    @PutMapping("/{id}/read")
    public ResponseData<Void> markAsRead(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        log.info("Mark notification {} as read for user {}", id, user.getId());
        notificationService.markAsRead(id, user.getId());
        return new ResponseData<>(HttpStatus.OK.value(), "Notification marked as read", null);
    }

    /**
     * PUT /notifications/read-all - Mark all notifications as read
     */
    @PutMapping("/read-all")
    public ResponseData<Void> markAllAsRead(@AuthenticationPrincipal User user) {
        log.info("Mark all notifications as read for user {}", user.getId());
        notificationService.markAllAsRead(user.getId());
        return new ResponseData<>(HttpStatus.OK.value(), "All notifications marked as read", null);
    }
}
