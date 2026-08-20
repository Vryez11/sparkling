package com.dandi.sparkling.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DeleteCommentResponse {

    private final Long commentId;
    private final LocalDateTime deletedAt;

    private DeleteCommentResponse(Long commentId, LocalDateTime deletedAt) {
        this.commentId = commentId;
        this.deletedAt = deletedAt;
    }

    public static DeleteCommentResponse from(Long commentId, LocalDateTime deletedAt) {
        return new DeleteCommentResponse(commentId, deletedAt);
    }
}
