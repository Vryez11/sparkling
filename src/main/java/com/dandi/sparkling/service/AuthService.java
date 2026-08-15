package com.dandi.sparkling.service;

import com.dandi.sparkling.dto.LoginRequest;
import com.dandi.sparkling.dto.LoginResponse;
import com.dandi.sparkling.dto.RefreshRequest;
import com.dandi.sparkling.dto.RefreshResponse;
import com.dandi.sparkling.entity.RefreshToken;
import com.dandi.sparkling.entity.User;
import com.dandi.sparkling.exception.InvalidRefreshTokenException;
import com.dandi.sparkling.repository.RefreshTokenRepository;
import com.dandi.sparkling.repository.UserRepository;
import com.dandi.sparkling.config.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("가입되지 않은 이메일입니다."));

        boolean isMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!isMatches) {
            throw new RuntimeException("올바른 비밀번호를 입력하세요.");
        }

        String accessToken = jwtProvider.createAccessToken(user.getId());
        String refreshToken = issueRefreshToken(user.getId());

        return LoginResponse.from(user, accessToken, refreshToken);
    }

    @Transactional(noRollbackFor = InvalidRefreshTokenException.class)
    public RefreshResponse refresh(RefreshRequest request) {

        Long userId = jwtProvider.parseRefreshToken(request.getRefreshToken());

        RefreshToken saved = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(InvalidRefreshTokenException::new);

        if (!saved.getToken().equals(request.getRefreshToken())) {
            refreshTokenRepository.delete(saved);
            throw new InvalidRefreshTokenException();
        }

        String accessToken = jwtProvider.createAccessToken(userId);
        String refreshToken = jwtProvider.createRefreshToken(userId);
        saved.rotate(refreshToken);

        return RefreshResponse.of(accessToken, refreshToken);
    }

    private String issueRefreshToken(Long userId) {

        String refreshToken = jwtProvider.createRefreshToken(userId);

        refreshTokenRepository.findByUserId(userId)
                .ifPresentOrElse(
                        saved -> saved.rotate(refreshToken),
                        () -> refreshTokenRepository.save(RefreshToken.builder()
                                .userId(userId)
                                .token(refreshToken)
                                .build()));

        return refreshToken;
    }
}
