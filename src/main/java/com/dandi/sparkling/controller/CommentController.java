package com.dandi.sparkling.controller;

import com.dandi.sparkling.config.security.CurrentUserId;
import com.dandi.sparkling.dto.CreateCommentRequest;
import com.dandi.sparkling.dto.CreateCommentResponse;
import com.dandi.sparkling.dto.DeleteCommentResponse;
import com.dandi.sparkling.dto.GetCommentListResponse;
import com.dandi.sparkling.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CreateCommentResponse> create(
            @PathVariable Long postId,
            @CurrentUserId Long userId,
            @Valid @RequestBody CreateCommentRequest request
    ) {
        CreateCommentResponse response = commentService.createComment(userId, postId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<GetCommentListResponse> list(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentList(postId));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<DeleteCommentResponse> delete(
            @PathVariable Long commentId,
            @CurrentUserId Long userId
    ) {
        return ResponseEntity.ok(commentService.delete(userId, commentId));
    }
}
