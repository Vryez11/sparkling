package com.dandi.sparkling.dto;

import lombok.Data;

@Data
public class CreatePostResponse {

    private CreatePostResponse(Long postId) {
        this.postId = postId;
    }

    Long postId;

    public static CreatePostResponse from(Long postId) {
        return new CreatePostResponse(postId);
    }
}
