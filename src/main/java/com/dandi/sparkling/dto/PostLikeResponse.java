package com.dandi.sparkling.dto;

import lombok.Getter;

@Getter
public class PostLikeResponse {

    private final Long postId;
    private final int likeCount;

    private PostLikeResponse(Long postId, int likeCount) {
        this.postId = postId;
        this.likeCount = likeCount;
    }

    public static PostLikeResponse from(Long postId, int likeCount) {
        return new PostLikeResponse(postId, likeCount);
    }
}
