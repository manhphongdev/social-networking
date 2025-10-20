package vn.socialnet.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.socialnet.dto.request.SignInRequest;
import vn.socialnet.dto.response.LoginResponse;
import vn.socialnet.dto.response.ResponseData;
import vn.socialnet.dto.response.TokenResponse;
import vn.socialnet.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Authentication Controller")
@Slf4j(topic = "AUTHENTICATION-CONTROLLER")
public class AuthenticationController {
    AuthenticationService authenticationService;

    @Operation(summary = "Access token", description = "Get access token and refresh token by email and password")
    @PostMapping("/log-in")
    public ResponseData<LoginResponse> getAccessToken(@RequestBody @Valid SignInRequest req) {
        log.info("Access token request: {}", req);
        return new ResponseData<>(HttpStatus.OK.value(), "Login successful", authenticationService.getAccessToken(req));
    }

    @Operation(summary = "Refresh token", description = "Get access token and refresh token by email and password")
    @PostMapping("/refresh-token")
    public TokenResponse getRefreshToken(@RequestBody @Valid SignInRequest req) {
        log.info("Refresh token request, refresh token: {}", req);
        return authenticationService.getRefreshToken(req);
    }
}
