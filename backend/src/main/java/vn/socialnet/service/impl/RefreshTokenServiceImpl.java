package vn.socialnet.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.socialnet.model.RefreshToken;
import vn.socialnet.model.User;
import vn.socialnet.repository.RefreshTokenRepository;
import vn.socialnet.service.RefreshTokenService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void replaceRefreshToken(User user, String token, long expirySeconds) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUser(user);

        if (optionalRefreshToken.isPresent()) {
            RefreshToken refreshToken = optionalRefreshToken.get();
            refreshToken.setToken(token);
            refreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(expirySeconds));
            refreshTokenRepository.save(refreshToken);
        } else {
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setUser(user);
            refreshToken.setToken(token);
            refreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(expirySeconds));
            refreshTokenRepository.save(refreshToken);
        }
    }
}
