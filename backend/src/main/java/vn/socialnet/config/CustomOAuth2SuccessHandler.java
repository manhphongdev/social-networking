package vn.socialnet.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import vn.socialnet.enums.AuthProvider;
import vn.socialnet.enums.UserRole;
import vn.socialnet.enums.UserStatus;
import vn.socialnet.model.AccountProvider;
import vn.socialnet.model.Role;
import vn.socialnet.model.User;
import vn.socialnet.repository.AccountProviderRepository;
import vn.socialnet.repository.RoleRepository;
import vn.socialnet.repository.UserRepository;
import vn.socialnet.service.JWTService;
import vn.socialnet.service.RefreshTokenService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AccountProviderRepository accountProviderRepository;
    private final RefreshTokenService refreshTokenService;

    private static final String REDIRECT_URI = "http://localhost:3000/customer";

    /**
     * Handles successful OAuth2 authentication.
     *
     * <p>This method is invoked when Spring Security completes an OAuth2 login flow successfully.
     * It ensures that the authenticated user is properly linked to an internal account, creates
     * the account if necessary, attaches the OAuth2 provider, generates JWT tokens, and finally
     * redirects the user to the frontend with the tokens.</p>
     *
     * <p>Processing steps:</p>
     * <ol>
     *   <li>Extract user information from {@link OAuth2AuthenticationToken}.</li>
     *   <li>Find the account in the database by email.</li>
     *   <li>If found, verify that the account has the GOOGLE provider; create it if missing.</li>
     *   <li>If not found:
     *       <ul>
     *         <li>Block the request if the path is restricted (e.g., "/google-admin").</li>
     *         <li>Otherwise, create a new account with default CUSTOMER role and attach GOOGLE provider.</li>
     *       </ul>
     *   </li>
     *   <li>Generate an access token and refresh token using {@link JWTService}.</li>
     *   <li>Redirect to the frontend callback URL with the tokens as query parameters.</li>
     * </ol>
     *
     * @param request        the current HTTP request
     * @param response       the current HTTP response
     * @param authentication the {@link Authentication} object, expected to be an instance of
     *                       {@link OAuth2AuthenticationToken} containing the authenticated user
     * @throws IOException      if an input or output error occurs
     * @throws ServletException if a servlet error occurs during handling
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        Map<String, Object> attributes = authToken.getPrincipal().getAttributes();

        // information google returns
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String sub = (String) attributes.get("sub");

        String registrationId = authToken.getAuthorizedClientRegistrationId(); // "google-user" or "google-admin"

        // 1. Find account by email
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user;

        if (userOptional.isPresent()) {
            user = userOptional.get();

            // 1. check account status
            if (user.getStatus() == UserStatus.BANNED) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Account inactive or deleted. Please contact support.");
                return;
            }

            // 2. Đảm bảo account có provider GOOGLE
            accountProviderRepository.findByUserAndProvider(user, AuthProvider.GOOGLE)
                    .orElseGet(() -> {
                        AccountProvider googleProvider = AccountProvider.builder()
                                .user(user)
                                .provider(AuthProvider.GOOGLE)
                                .providerId(sub)
                                .build();
                        return accountProviderRepository.save(googleProvider);
                    });

        } else {
            //if not exist account -> new account
            if ("google-admin".equals(registrationId)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Admin must exist before you can login");
                return;
            }

            Role role = roleRepository.findByName(UserRole.USER.name())
                    .orElseThrow(() -> new RuntimeException("Role not found"));


            user = User.builder()
                    .name(name)
                    .email(email)
                    .role(role)
                    .status(UserStatus.ACTIVE)
                    .build();

            AccountProvider googleProvider = AccountProvider.builder()
                    .user(user)
                    .provider(AuthProvider.GOOGLE)
                    .providerId(sub)
                    .build();
            user.setAccountProviders(List.of(googleProvider));
            userRepository.save(user);
        }


        // 4. create refresh token
        String refreshToken = jwtService.generateRefreshToken(
                email,
                List.of(user.getRole().getName())
        );


        //refreshTokenService.replaceRefreshToken(user, refreshToken, jwtService.getRefreshTokenExpiryInSecond());

        // Set refresh token as HttpOnly cookie
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        //refreshCookie.setMaxAge((int) jwtService.getRefreshTokenExpiryInSecond());
        response.addCookie(refreshCookie);

        response.sendRedirect(REDIRECT_URI);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }
}
