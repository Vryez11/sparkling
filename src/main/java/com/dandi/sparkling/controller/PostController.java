package com.dandi.sparkling.controller;

import com.dandi.sparkling.config.security.CurrentUserId;
import com.dandi.sparkling.dto.CreatePostRequest;
import com.dandi.sparkling.dto.CreatePostResponse;
import com.dandi.sparkling.dto.DeletePostResponse;
import com.dandi.sparkling.dto.GetPostListResponse;
import com.dandi.sparkling.dto.PostDetailResponse;
import com.dandi.sparkling.dto.UpdatePostRequest;
import com.dandi.sparkling.service.PostService;
import jakarta.validation.Valid;
import java.net.URI;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<CreatePostResponse> create(
            @CurrentUserId Long userId,
            @Valid @RequestBody CreatePostRequest request
    ) {
        CreatePostResponse response = postService.createPost(userId, request);

        return ResponseEntity
                .created(URI.create("/posts/" + response.getPostId()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<GetPostListResponse> list() {

        GetPostListResponse response = postService.getPostList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> detail(
            @PathVariable Long postId
    ) {

        PostDetailResponse response = postService.postDetail(postId);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> update(
            @PathVariable Long postId,
            @CurrentUserId Long userId,
            @Valid @RequestBody UpdatePostRequest request
    ) {

        PostDetailResponse response = postService.updatePost(userId, postId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<DeletePostResponse> delete(
            @PathVariable Long postId,
            @CurrentUserId Long userId
    ) {

        DeletePostResponse response = postService.delete(userId, postId);

        return ResponseEntity.ok(response);
    }
}
