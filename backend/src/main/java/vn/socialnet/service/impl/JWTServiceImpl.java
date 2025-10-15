package vn.socialnet.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import vn.socialnet.enums.TokenType;
import vn.socialnet.service.JWTService;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


@Service
@Slf4j(topic = "JWT-SERVICE")
public class JWTServiceImpl implements JWTService {

    @Value("${jwt.key_access_token}")
    String ACCESS_TOKEN_KEY;

    @Value("${jwt.key_refresh_token}")
    String REFRESH_TOKEN_KEY;

    @Value("${jwt.access_token_expiration}")
    long accessTokenExpiration;

    @Override
    public String generateAccessToken(String email, List<String> authorities) {
        log.info("Generate access token for  and email {}", email);
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", authorities);

        return generateToken(claims, email);
    }

    @Override
    public String generateRefreshToken(String email, List<String> authorities) {
        log.info("Generate refresh token for  and email {}", email);
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", authorities);

        return generateRefreshToken(claims, email);
    }

    @Override
    public String extractUsername(String token, TokenType tokenType) {
        log.info("Extract username from access token {}", token);

        return extractClaims(tokenType, token, claims -> claims.getSubject());
    }

    @Override
    public long getAccessTokenExpirationInSeconds() {
        return accessTokenExpiration;
    }

    private <T> T extractClaims(TokenType type, String token, Function<Claims, T> claimsExtractor) {
        final Claims claims = extractAllClaims(type, token);
        return claimsExtractor.apply(claims);
    }

    private Claims extractAllClaims(TokenType type, String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey(type))
                    .build()
                    .parseClaimsJws(token).getBody();
        } catch (SignatureException | ExpiredJwtException e) {
            throw new AccessDeniedException("Access denied!, error: " + e.getMessage());
        }
    }

    private String generateToken(Map<String, Object> claims, String email) {
        log.info("Generate token for email {}", email);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))//1h
                .signWith(getKey(TokenType.ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateRefreshToken(Map<String, Object> claims, String email) {
        log.info("Generate token for email {}", email);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration * 24 * 30)) //30 day
                .signWith(getKey(TokenType.REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }


    private Key getKey(TokenType type) {
        switch (type) {
            case ACCESS_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(ACCESS_TOKEN_KEY));
            }
            case REFRESH_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(REFRESH_TOKEN_KEY));
            }
            default -> throw new IllegalArgumentException("Invalid token type");
        }
    }

}

