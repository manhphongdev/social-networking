package vn.socialnet.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import vn.socialnet.enums.NotificationTargetType;
import vn.socialnet.enums.NotificationType;

import java.time.LocalDateTime;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationResponse {
    Long id;
    Long userId;
    Long fromUserId;
    String fromUserName;
    String fromUserAvatar;
    NotificationType type;
    NotificationTargetType targetType;
    Long targetId;
    String text;
    boolean isReadded;
    LocalDateTime createdAt;
}
