package com.dandi.sparkling.controller;

import com.dandi.sparkling.dto.CreatePostRequest;
import com.dandi.sparkling.dto.CreatePostResponse;
import com.dandi.sparkling.service.PostService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<CreatePostResponse> post(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CreatePostRequest request
    ) {
        CreatePostResponse response = postService.createPost(userId, request);

        return ResponseEntity
                .created(URI.create("/posts/" + response.getPostId()))
                .body(response);
    }
}
