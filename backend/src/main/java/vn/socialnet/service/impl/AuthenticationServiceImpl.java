package vn.socialnet.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.socialnet.dto.request.SignInRequest;
import vn.socialnet.dto.response.LoginResponse;
import vn.socialnet.dto.response.TokenResponse;
import vn.socialnet.dto.response.UserDetailResponse;
import vn.socialnet.model.User;
import vn.socialnet.repository.UserRepository;
import vn.socialnet.service.AuthenticationService;
import vn.socialnet.service.JWTService;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j(topic = "AUTHENTICATION-SERVICE")
public class AuthenticationServiceImpl implements AuthenticationService {

    UserRepository userRepository;
    JWTService jwtService;
    AuthenticationManager authenticationManager;

    @Override
    public LoginResponse getAccessToken(SignInRequest request) {
        log.debug("getAccessToken");

        List<String> authorities = new ArrayList<>();

        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(),
                            request.getPassword()));

            log.info("isAuthenticated={}", authenticate.isAuthenticated());
            log.info("authorities={}", authenticate.getAuthorities());

            authorities.add(authenticate.getAuthorities().toString());

            SecurityContextHolder.getContext().setAuthentication(authenticate);

        } catch (DisabledException e) {
            log.error("Account is disabled: {}", e.getMessage());
            throw new AuthenticationServiceException("Account is disabled. Please contact administrator.");
        } catch (LockedException e) {
            log.error("Account is locked: {}", e.getMessage());
            throw new AuthenticationServiceException("Account is locked. Please contact administrator.");
        } catch (AccountExpiredException e) {
            log.error("Account has expired: {}", e.getMessage());
            throw new AuthenticationServiceException("Account has expired. Please contact administrator.");
        } catch (CredentialsExpiredException e) {
            log.error("Credentials have expired: {}", e.getMessage());
            throw new AuthenticationServiceException("Your password has expired. Please reset your password.");
        } catch (BadCredentialsException e) {
            log.error("Invalid credentials: {}", e.getMessage());
            throw new AuthenticationServiceException("Invalid email or password.");
        } catch (AuthenticationException e) {
            log.error("Authentication failed: {}", e.getMessage());
            throw new AuthenticationServiceException("Authentication failed. Please try again.");
        }

        String accessToken = jwtService.generateAccessToken(request.getEmail(), authorities);
        String refreshToken = jwtService.generateRefreshToken(request.getEmail(), authorities);

        long accessTokenExpiresIn = jwtService.getAccessTokenExpirationInSeconds() / 1000;

        TokenResponse token = TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(accessTokenExpiresIn)
                .build();

        User user = userRepository.findByEmail(request.getEmail()).get();

        UserDetailResponse userDetail = UserDetailResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .roleName(user.getRole().getName())
                .status(user.getStatus())
                .build();

        log.info("accessToken={}", accessToken);
        log.info("login successful");

        return new LoginResponse(token, userDetail);
    }

    @Override
    public TokenResponse getRefreshToken(SignInRequest request) {
        return null;
    }


}
