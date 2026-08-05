package com.dandi.sparkling.post.update;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateEndPoint {

    private final UpdateHandler updateHandler;

    @PatchMapping("/posts/{id}")
    public ResponseEntity<UpdateResponse> update(
            @PathVariable("id") Long postId,
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody UpdateRequest request
    ) {
        UpdateResponse response = updateHandler.update(userId, postId, request);

        return ResponseEntity
                .ok(response);
    }
}
