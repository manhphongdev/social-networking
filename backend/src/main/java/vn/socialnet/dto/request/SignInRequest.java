package vn.socialnet.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.socialnet.utils.fieldValidator.Email;
import vn.socialnet.utils.fieldValidator.Password;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignInRequest implements Serializable {

    @Email()
    @Schema(example = "user@test.com", defaultValue = "user@test.com")
    @NotNull
    String email;

    @Password
    @Schema(example = "12345678", defaultValue = "12345678")
    String password;

}
