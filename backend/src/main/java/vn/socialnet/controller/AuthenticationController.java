package vn.socialnet.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.socialnet.dto.request.AuthenticationRequest;
import vn.socialnet.dto.request.IntrospectRequest;
import vn.socialnet.dto.response.AuthenticationResponse;
import vn.socialnet.dto.response.IntrospectResponse;
import vn.socialnet.dto.response.ResponseData;
import vn.socialnet.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/log-in")
    public ResponseData<AuthenticationResponse> login(@RequestBody AuthenticationRequest req) {
        AuthenticationResponse responseValue;
        responseValue = authenticationService.authenticate(req);
        return new ResponseData<>(HttpStatus.OK.value(), "Login successful", responseValue);
    }

    @PostMapping("/introspect")
    public ResponseData<IntrospectResponse> login(@RequestBody IntrospectRequest req) {
        IntrospectResponse responseValue = authenticationService.introspect(req);
        if (responseValue.isValid()) {
            return new ResponseData<>(HttpStatus.OK.value(), "Introspect successful", responseValue);
        } else {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Introspect failed", responseValue);
        }
    }

}
