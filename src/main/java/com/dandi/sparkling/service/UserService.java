package com.dandi.sparkling.service;

import com.dandi.sparkling.dto.UserRegisterRequest;
import com.dandi.sparkling.dto.UserRegisterResponse;
import com.dandi.sparkling.entity.User;
import com.dandi.sparkling.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserRegisterResponse register(UserRegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("존재하는 이메일입니다. ");
        }

        if (userRepository.existsByNickname(request.getNickname())) {
            throw new RuntimeException("존재하는 닉네임입니다.");
        }

        String encoded = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(encoded)
                .build();

        return new UserRegisterResponse().from(userRepository.save(user));
    }
}
