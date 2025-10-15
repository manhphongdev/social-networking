package vn.socialnet.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.socialnet.enums.Gender;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class UserProfileResponse {

    String name;

    LocalDate dateOfBirth;

    Gender gender;

    String avatarUrl;

    String bio;

    String location;

    int totalFollowers;
    int totalFollowing;

    LocalDate jointAt;

    int totalPost;

}
