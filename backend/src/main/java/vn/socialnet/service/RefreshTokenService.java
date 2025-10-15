package vn.socialnet.service;

import vn.socialnet.model.User;

public interface RefreshTokenService {
    void replaceRefreshToken(User user, String token, long expirySeconds);
}
