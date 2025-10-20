package vn.socialnet.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import vn.socialnet.enums.ConversationType;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConversationResponse {
    Long id;
    ConversationType type;
    List<ParticipantInfo> participants;
    MessageResponse lastMessage;
    Long unreadCount;
    LocalDateTime createdAt;
}
