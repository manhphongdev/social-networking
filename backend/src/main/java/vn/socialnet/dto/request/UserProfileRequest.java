package vn.socialnet.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.socialnet.enums.EnumPattern;
import vn.socialnet.enums.Gender;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileRequest {

    @NotNull(message = "User name must be not blank")
    @Schema(example = "Manh Phong", defaultValue = "Manh Phong")
    String name;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotNull(message = "Date must be not null!")
    @Past(message = "Date of birth must be in the past!")
    @Schema(description = "example: 19/08/2005",
            example = "19/08/2005",
            defaultValue = "19/08/2005")
    LocalDate dateOfBirth;

    @NotNull(message = "Gender cannot be null")
    @EnumPattern(name = "gender", regexp = "MALE|FEMALE|OTHER")
    @Schema(description = "Must be in MALE|FEMALE|OTHER",
            example = "MALE",
            defaultValue = "MALE")
    Gender gender;

    String avatarUrl;


}
