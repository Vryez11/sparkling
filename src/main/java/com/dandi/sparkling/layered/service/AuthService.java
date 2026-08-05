package com.dandi.sparkling.layered.service;

import com.dandi.sparkling.layered.dto.LoginRequest;
import com.dandi.sparkling.layered.dto.LoginResponse;
import com.dandi.sparkling.user.share.User;
import com.dandi.sparkling.user.share.UserRepository;
import com.dandi.sparkling.config.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("가입되지 않은 이메일입니다."));

        boolean isMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!isMatches) {
            throw new RuntimeException("올바른 비밀번호를 입력하세요.");
        }

        String token = jwtProvider.createToken(user.getId());

        return LoginResponse.from(user, token);
    }
}
