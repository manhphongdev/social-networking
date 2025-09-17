package vn.socialnet.service;

import vn.socialnet.enums.TokenType;

import java.util.List;

public interface JWTService {
    String generateAccessToken(String email, List<String> authorities);

    String generateRefreshToken(String email, List<String> authorities);

    String extractUsername(String token, TokenType tokenType);
}
