package com.dandi.sparkling.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DeletePostResponse {

    private final Long postId;
    private final LocalDateTime deletedAt;

    private DeletePostResponse(Long postId, LocalDateTime deletedAt) {
        this.postId = postId;
        this.deletedAt = deletedAt;
    }

    public static DeletePostResponse from(Long postId, LocalDateTime deletedAt) {
        return new DeletePostResponse(postId, deletedAt);
    }
}
