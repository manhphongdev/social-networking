package vn.socialnet.service;

import vn.socialnet.dto.request.AuthenticationRequest;
import vn.socialnet.dto.request.IntrospectRequest;
import vn.socialnet.dto.response.AuthenticationResponse;
import vn.socialnet.dto.response.IntrospectResponse;


public interface AuthenticationService {

    AuthenticationResponse authenticate(AuthenticationRequest req);

    IntrospectResponse introspect(IntrospectRequest req);
}
