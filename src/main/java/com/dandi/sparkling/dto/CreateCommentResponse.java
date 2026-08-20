package com.dandi.sparkling.dto;

import lombok.Getter;

@Getter
public class CreateCommentResponse {

    private final Long commentId;

    private CreateCommentResponse(Long commentId) {
        this.commentId = commentId;
    }

    public static CreateCommentResponse from(Long commentId) {
        return new CreateCommentResponse(commentId);
    }
}
