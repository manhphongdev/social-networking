package vn.socialnet.service;

import vn.socialnet.dto.request.AuthenticationRequest;
import vn.socialnet.dto.request.IntrospectRequest;
import vn.socialnet.dto.request.SignInRequest;
import vn.socialnet.dto.response.AuthenticationResponse;
import vn.socialnet.dto.response.IntrospectResponse;
import vn.socialnet.dto.response.TokenResponse;


public interface AuthenticationService {


    TokenResponse getAccessToken(SignInRequest request);

    TokenResponse getRefreshToken(SignInRequest request);
}
