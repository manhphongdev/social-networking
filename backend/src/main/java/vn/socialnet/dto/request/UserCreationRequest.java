package vn.socialnet.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.socialnet.enums.EnumPattern;
import vn.socialnet.enums.Gender;
import vn.socialnet.utils.fieldValidator.Email;
import vn.socialnet.utils.fieldValidator.Password;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest implements Serializable {

    @Email
    @Schema(example = "user1@gmail.com", defaultValue = "user1@gmail.com")
    String email;

    @Password
    @Schema(example = "12345678", defaultValue = "12345678")
    String password;

    @NotNull(message = "User name must be not blank")
    @Schema(example = "Manh Phong", defaultValue = "Manh Phong")
    String name;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotNull(message = "Date must be not null!")
    @Past(message = "Date of birth must be in the past!")
    @Schema(description = "example: 20/08/2005",
            example = "20/08/2005",
            defaultValue = "20/08/2005")
    LocalDate dateOfBirth;

    @NotNull(message = "Gender cannot be null")
    @EnumPattern(name = "gender", regexp = "MALE|FEMALE|OTHER")
    @Schema(description = "Must be in MALE|FEMALE|OTHER",
            example = "MALE",
            defaultValue = "MALE")
    Gender gender;
}
