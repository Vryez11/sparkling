package com.dandi.sparkling.controller;

import com.dandi.sparkling.config.security.CurrentUserId;
import com.dandi.sparkling.dto.GetPostListResponse;
import com.dandi.sparkling.dto.UserMeResponse;
import com.dandi.sparkling.dto.UserRegisterRequest;
import com.dandi.sparkling.dto.UserRegisterResponse;
import com.dandi.sparkling.service.PostService;
import com.dandi.sparkling.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PostService postService;

    @PostMapping()
    public ResponseEntity<UserRegisterResponse> register(
            @Valid @RequestBody UserRegisterRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.register(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserMeResponse> me(
            @CurrentUserId Long userId
    ) {

        return ResponseEntity
                .ok()
                .body(userService.getMyInfo(userId));
    }

    @GetMapping("/me/posts")
    public ResponseEntity<GetPostListResponse> myPosts(
            @CurrentUserId Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity
                .ok()
                .body(postService.getMyPostList(userId, page, size));
    }
}
