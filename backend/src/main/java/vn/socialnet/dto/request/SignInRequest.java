package vn.socialnet.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignInRequest implements Serializable {

    String email;
    String password;
    String platform; //web, mobile, miniApp
    String token;
    String deviceToken;
    String versionApp;


}
