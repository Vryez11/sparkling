package com.dandi.sparkling.post.delete;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeleteEndPoint {

    private final DeleteHandler deleteHandler;

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<DeleteResponse> delete(
            @PathVariable("id") Long postId,
            @AuthenticationPrincipal Long userId
    ) {
        DeleteResponse response = deleteHandler.delete(userId, postId);

        return ResponseEntity.ok(response);
    }
}
