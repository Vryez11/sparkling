package com.dandi.sparkling.controller;

import com.dandi.sparkling.dto.LoginRequest;
import com.dandi.sparkling.dto.LoginResponse;
import com.dandi.sparkling.dto.RefreshRequest;
import com.dandi.sparkling.dto.RefreshResponse;
import com.dandi.sparkling.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        LoginResponse response = authService.login(request);

        return ResponseEntity
                .ok()
                .body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(
            @Valid @RequestBody RefreshRequest request
    ) {

        RefreshResponse response = authService.refresh(request);

        return ResponseEntity
                .ok()
                .body(response);
    }
}
