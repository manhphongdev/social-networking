package vn.socialnet.service;

import vn.socialnet.dto.request.SignInRequest;
import vn.socialnet.dto.response.LoginResponse;
import vn.socialnet.dto.response.TokenResponse;


public interface AuthenticationService {


    LoginResponse getAccessToken(SignInRequest request);

    TokenResponse getRefreshToken(SignInRequest request);
}
