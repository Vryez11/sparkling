package com.dandi.sparkling.user.getme;

import com.dandi.sparkling.user.share.User;
import com.dandi.sparkling.user.share.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetMeEndPoint {

    private final UserRepository userRepository;

    @GetMapping("/users/me")
    public ResponseEntity<GetMeResponse> me(
            @AuthenticationPrincipal Long userId
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        return ResponseEntity
                .ok()
                .body(GetMeResponse.from(user));
    }
}
