package com.dandi.sparkling.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class JwtProvider {

    private final JwtEncoder jwtEncoder;
    private final long accessExpirationMs;
    private final long refreshExpirationMs;

    public JwtProvider(JwtEncoder jwtEncoder,
                       @Value("${jwt.expiration-ms}") long accessExpirationMs,
                       @Value("${jwt.refresh-expiration-ms}") long refreshExpirationMs) {
        this.jwtEncoder = jwtEncoder;
        this.accessExpirationMs = accessExpirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    public String createAccessToken(Long userId) {
        return createToken(userId, "access", accessExpirationMs);
    }

    public String createRefreshToken(Long userId) {
        return createToken(userId, "refresh", refreshExpirationMs);
    }

    private String createToken(Long userId, String type, long expirationMs) {

        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(String.valueOf(userId))
                .claim("type", type)
                // iat/exp가 초 단위라 같은 초에 발급된 토큰이 완전히 동일해진다 — jti로 토큰마다 유일성을 보장 (회전 감지의 전제)
                .id(UUID.randomUUID().toString())
                .issuedAt(now)
                .expiresAt(now.plusMillis(expirationMs))
                .build();

        // 헤더에 HS256을 명시하지 않으면 기본값 RS256으로 대칭키를 찾지 못해 발급이 실패한다
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}
