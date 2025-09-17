package vn.socialnet.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.crypto.spec.SecretKeySpec;
import java.util.List;

@Profile(("dev"))
@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    private final String[] PUBLIC_ENDPOINTS = {
            "/users/swagger-config",
            "/auth/log-in", "auth/introspect",
            "/swagger-ui.html",
    };
    @Value("${jwt.signer-key}")
    private String jwtSignerKey;

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
////        httpSecurity.authorizeHttpRequests(request ->
////                request.requestMatchers(HttpMethod.POST, "/users/swagger-config").permitAll()
////                        .requestMatchers(HttpMethod.POST, "/auth/log-in", "auth/introspect").permitAll()
////                        .anyRequest().authenticated());
//
//        httpSecurity.authorizeHttpRequests(request ->
//                request.anyRequest().permitAll());
//
//        httpSecurity.oauth2ResourceServer(oauth2 ->
//                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()))
//
//        );
//
//        httpSecurity.csrf(AbstractHttpConfigurer::disable);
//
//        return httpSecurity.build();
//    }

    @Bean
    JwtDecoder jwtDecoder() {
        //create secret key
        SecretKeySpec secretKeySpec = new SecretKeySpec(jwtSignerKey.getBytes(), "HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }



    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Áp dụng cho tất cả endpoint
                .allowedOrigins("http://localhost:3000")  // Origin của frontend React (thay đổi nếu cần)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Các method cho phép
                .allowedHeaders("*")  // Cho phép tất cả headers (bao gồm Content-Type, Authorization)
                .allowCredentials(true);  // Nếu cần gửi cookie hoặc auth headers
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    
}
