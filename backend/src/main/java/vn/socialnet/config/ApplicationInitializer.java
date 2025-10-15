package vn.socialnet.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.socialnet.enums.Gender;
import vn.socialnet.enums.UserRole;
import vn.socialnet.enums.UserStatus;
import vn.socialnet.exception.ResourceNotFoundException;
import vn.socialnet.model.Role;
import vn.socialnet.model.User;
import vn.socialnet.repository.RoleRepository;
import vn.socialnet.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplicationInitializer {

    final RoleRepository roleRepository;
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;

    @Value("${admin.email_default}")
    String EMAIL_ADMIN;

    @Value("${admin.password_default}")
    String PASSWORD_ADMIN;

    @Value("${user-account-test.email}")
    String EMAIL_USER;

    @Value("${user-account-test.password}")
    String PASSWORD_USER;

    @Bean
    public ApplicationRunner initRole() {
        return app -> {
            if (roleRepository.findByName(UserRole.USER.name()).isEmpty()) {
                roleRepository.save(
                        new Role(null, UserRole.USER.name(),
                                "The customer using system",
                                new HashSet<>(),
                                LocalDateTime.now()));
            }

            if (roleRepository.findByName(UserRole.ADMIN.name()).isEmpty()) {
                roleRepository.save(
                        new Role(null, UserRole.ADMIN.name(),
                                "The admin of system",
                                new HashSet<>(),
                                LocalDateTime.now()));
            }

            if (userRepository.findByEmail(EMAIL_ADMIN).isEmpty()) {
                Role role = roleRepository.findByName(UserRole.ADMIN.name()).orElseThrow(() ->
                        new ResourceNotFoundException("Role name not exist!"));

                User user = User.builder()
                        .email(EMAIL_ADMIN)
                        .password(passwordEncoder.encode(PASSWORD_ADMIN))
                        .name("System Admin")
                        .status(UserStatus.ACTIVE)
                        .dateOfBirth(LocalDate.now())
                        .role(role)
                        .gender(Gender.MALE)
                        .build();
                user.setCreatedAt(LocalDateTime.now());
                user.setUpdatedAt(LocalDateTime.now());
                userRepository.save(user);
            }

            if (userRepository.findByEmail(EMAIL_USER).isEmpty()) {
                Role role = roleRepository.findByName(UserRole.USER.name()).orElseThrow(() ->
                        new ResourceNotFoundException("Role name not exist!"));

                User user = User.builder()
                        .email(EMAIL_USER)
                        .password(passwordEncoder.encode(PASSWORD_USER))
                        .name("Customer User")
                        .status(UserStatus.ACTIVE)
                        .role(role)
                        .dateOfBirth(LocalDate.now())
                        .gender(Gender.MALE)
                        .build();
                userRepository.save(user);
            }
        };
    }
}
