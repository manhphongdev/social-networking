package vn.socialnet.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import vn.socialnet.enums.MediaType;

import java.time.LocalDateTime;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageResponse {
    Long id;
    Long conversationId;
    Long userId;
    String userName;
    String userAvatar;
    String message;
    String mediaUrl;
    MediaType mediaType;
    Boolean isRead;
    LocalDateTime createdAt;
}
