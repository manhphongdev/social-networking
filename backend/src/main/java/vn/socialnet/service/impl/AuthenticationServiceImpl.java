package vn.socialnet.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.socialnet.dto.request.AuthenticationRequest;
import vn.socialnet.dto.request.IntrospectRequest;
import vn.socialnet.dto.response.AuthenticationResponse;
import vn.socialnet.dto.response.IntrospectResponse;
import vn.socialnet.exception.AuthenticatedException;
import vn.socialnet.model.Role;
import vn.socialnet.model.User;
import vn.socialnet.repository.UserRepository;
import vn.socialnet.service.AuthenticationService;
import vn.socialnet.utils.message.AuthMessage;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    UserRepository userRepository;
    @NonFinal
    @Value("${jwt.signer-key}")
    String SIGNER_KEY;

    public IntrospectResponse introspect(IntrospectRequest req) {
        var token = req.getToken();

        JWSVerifier verifier = null;
        try {
            verifier = new MACVerifier(SIGNER_KEY.getBytes());

            SignedJWT signedJWT = SignedJWT.parse(token);

            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            var verified = signedJWT.verify(verifier);

            return IntrospectResponse.builder()
                    .valid(verified && expirationTime.after(new Date()))
                    .build();

        } catch (JOSEException | ParseException e) {
            throw new RuntimeException("Invalid token!");
        }
    }


    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest req) {
        //check email not exist
        User user = userRepository.findByEmail(req.getEmail()).orElseThrow(()
                -> new UsernameNotFoundException(AuthMessage.EMAIL_NOT_EXIST + " Email: " + req.getEmail()));
        log.info("User {} has been authenticated", user.getEmail());

        // check password matcher
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new AuthenticatedException(AuthMessage.PASSWORD_NOT_MATCH);
        }

        // create token
        try {
            String token = generateToken(user.getEmail(), user.getRoles());

            return AuthenticationResponse
                    .builder()
                    .authenticated(true)
                    .token(token)
                    .build();
        } catch (JOSEException e) {
            throw new RuntimeException("Could not generate JWT Token", e);
        }
    }

    private String generateToken(String email, Set<Role> roles) throws JOSEException {
        //b1: create header token
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        //b2: create payload
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer("socialnet.com")
                .subject(email)
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("roles", roles.stream().map(Role::getName).toList()).build();

        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        //b3: sign signature
        jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
        return jwsObject.serialize();
    }
}
