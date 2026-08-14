package com.dandi.sparkling.service;

import com.dandi.sparkling.dto.LoginRequest;
import com.dandi.sparkling.dto.LoginResponse;
import com.dandi.sparkling.dto.RefreshRequest;
import com.dandi.sparkling.dto.RefreshResponse;
import com.dandi.sparkling.entity.RefreshToken;
import com.dandi.sparkling.entity.User;
import com.dandi.sparkling.exception.RefreshTokenReusedException;
import com.dandi.sparkling.repository.RefreshTokenRepository;
import com.dandi.sparkling.repository.UserRepository;
import com.dandi.sparkling.config.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final JwtDecoder refreshTokenDecoder;

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

    @Transactional(noRollbackFor = RefreshTokenReusedException.class)
    public RefreshResponse refresh(RefreshRequest request) {

        Jwt jwt = decodeRefreshToken(request.getRefreshToken());
        Long userId = Long.valueOf(jwt.getSubject());

        RefreshToken saved = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 리프레시 토큰입니다."));

        // 서명은 유효한데 저장된 값과 다르면 이미 회전된 토큰 = 탈취 의심 → 세션 자체를 폐기
        if (!saved.getToken().equals(request.getRefreshToken())) {
            refreshTokenRepository.delete(saved);
            throw new RefreshTokenReusedException();
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

    private Jwt decodeRefreshToken(String token) {

        try {
            return refreshTokenDecoder.decode(token);
        } catch (JwtException e) {
            throw new RuntimeException("유효하지 않은 리프레시 토큰입니다.");
        }
    }
}
