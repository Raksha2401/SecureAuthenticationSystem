package org.authentication.service;

import org.authentication.entities.RefreshToken;
import org.authentication.entities.UserInfo;
import org.authentication.repository.RefreshTokenRepository;
import org.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    public RefreshToken createRefreshToken(String userName) {
        UserInfo userInfo = userRepository.findByUsername(userName);
        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(userInfo)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(60000))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if(token.getExpiryDate().compareTo(Instant.now())<0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + "Refresh token is expired. Please make a new Login..!");
        }
        return token;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public long countActiveTokens() {
        Iterable<RefreshToken> tokens = refreshTokenRepository.findAll();
        Instant now = Instant.now();
        long count = 0;
        for (RefreshToken token : tokens) {
            if (token.getExpiryDate().isAfter(now)) {
                count++;
            }
        }
        return count;
    }

}
