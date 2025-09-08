package vn.socialnet.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDetailResponse implements Serializable {
    Long id;
    String email;
    String name;
    LocalDate dateOfBirth;
    String gender;
    String status;
    List<String> roles;

    public UserDetailResponse(Long id, String email, String name, LocalDate dateOfBirth, String gender, String status, List<String> roles) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.status = status;
        this.roles = roles;
    }
}
