package com.dandi.sparkling.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class GetPostListResponse {

    private final List<PostResponse> posts;

    private GetPostListResponse(List<PostResponse> posts) {
        this.posts = posts;
    }

    public static GetPostListResponse from(List<PostResponse> posts) {
        return new GetPostListResponse(posts);
    }
}
