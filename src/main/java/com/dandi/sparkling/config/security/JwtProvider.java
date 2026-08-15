package com.dandi.sparkling.config.security;

import com.dandi.sparkling.exception.InvalidRefreshTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class JwtProvider {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder refreshTokenDecoder;
    private final long accessExpirationMs;
    private final long refreshExpirationMs;

    public JwtProvider(JwtEncoder jwtEncoder,
                       JwtDecoder refreshTokenDecoder,
                       @Value("${jwt.expiration-ms}") long accessExpirationMs,
                       @Value("${jwt.refresh-expiration-ms}") long refreshExpirationMs) {
        this.jwtEncoder = jwtEncoder;
        this.refreshTokenDecoder = refreshTokenDecoder;
        this.accessExpirationMs = accessExpirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    public String createAccessToken(Long userId) {
        return createToken(userId, "access", accessExpirationMs);
    }

    public String createRefreshToken(Long userId) {
        return createToken(userId, "refresh", refreshExpirationMs);
    }

    public Long parseRefreshToken(String token) {

        try {
            Jwt jwt = refreshTokenDecoder.decode(token);
            return Long.valueOf(jwt.getSubject());
        } catch (JwtException e) {
            throw new InvalidRefreshTokenException();
        }
    }

    private String createToken(Long userId, String type, long expirationMs) {

        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(String.valueOf(userId))
                .claim("type", type)
                .id(UUID.randomUUID().toString())
                .issuedAt(now)
                .expiresAt(now.plusMillis(expirationMs))
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}
