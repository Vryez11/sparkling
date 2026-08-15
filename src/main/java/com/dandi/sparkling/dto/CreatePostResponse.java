package com.dandi.sparkling.dto;

import lombok.Getter;

@Getter
public class CreatePostResponse {

    private final Long postId;

    private CreatePostResponse(Long postId) {
        this.postId = postId;
    }

    public static CreatePostResponse from(Long postId) {
        return new CreatePostResponse(postId);
    }
}
