package com.dandi.sparkling.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class GetCommentListResponse {

    private final List<CommentResponse> comments;

    private GetCommentListResponse(List<CommentResponse> comments) {
        this.comments = comments;
    }

    public static GetCommentListResponse from(List<CommentResponse> comments) {
        return new GetCommentListResponse(comments);
    }
}
