package vn.socialnet.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import vn.socialnet.enums.PostPrivates;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String caption;
    private PostPrivates privacy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    // User information
    private Long userId;
    private String userName;
    private String userAvatar;

    // Media
    private List<PostMediaResponse> media;

    // Engagement metrics
    private Map<String, Long> reactionCounts;
    private Long totalReactions;
    private Long commentCount;

    // Current user context
    private Boolean isOwner;
    private Boolean hasReacted;
    private String userReactionType;
}
