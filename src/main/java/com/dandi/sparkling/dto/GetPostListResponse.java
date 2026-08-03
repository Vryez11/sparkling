package com.dandi.sparkling.dto;

import lombok.Data;

import java.util.List;

@Data
public class GetPostListResponse {

    List<PostResponse> posts;

    private GetPostListResponse(List<PostResponse> posts) {
        this.posts = posts;
    }

    public static GetPostListResponse from (List<PostResponse> posts) {

        return new GetPostListResponse(posts);
    }
}
