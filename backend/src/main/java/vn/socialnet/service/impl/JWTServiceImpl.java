package vn.socialnet.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
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

import static vn.socialnet.enums.TokenType.ACCESS_TOKEN;
import static vn.socialnet.enums.TokenType.REFRESH_TOKEN;


@Service
@Slf4j(topic = "JWT-SERVICE")
public class JWTServiceImpl implements JWTService {


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
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))//1h
                .signWith(getKey(ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateRefreshToken(Map<String, Object> claims, String email) {
        log.info("Generate token for email {}", email);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) //1 day
                .signWith(getKey(REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey(TokenType type) {
        switch (type) {
            case ACCESS_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode("8mhZcWZVPhDeucdGaUQhUQoSkda3+jAtkBPJNP/4dvQ="));
            }
            case REFRESH_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode("Gu4jjMNk9oBnertRHqfRZJdHnz5jihbbnjAHeWNQbpg="));
            }
            default -> throw new IllegalArgumentException("Invalid token type");
        }
    }

}

