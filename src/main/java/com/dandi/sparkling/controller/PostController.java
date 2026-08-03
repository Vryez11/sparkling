package com.dandi.sparkling.controller;

import com.dandi.sparkling.dto.CreatePostRequest;
import com.dandi.sparkling.dto.CreatePostResponse;
import com.dandi.sparkling.dto.GetPostListResponse;
import com.dandi.sparkling.dto.PostResponse;
import com.dandi.sparkling.service.PostService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<GetPostListResponse> postList() {

        List<PostResponse> posts = postService.getPostList();

        return ResponseEntity
                .ok(GetPostListResponse.from(posts));
    }
}
