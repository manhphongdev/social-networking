package vn.socialnet.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSummaryResponse {
    Long id;
    String name;
    String username;
    String avatar;
    String bio;
    boolean isFollowing;
    int followersCount;
    int followingCount;
}
