package com.dandi.sparkling.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class GetPostListResponse {

    private final List<PostResponse> posts;
    private final PageInfoResponse pageInfo;

    private GetPostListResponse(List<PostResponse> posts, PageInfoResponse pageInfo) {
        this.posts = posts;
        this.pageInfo = pageInfo;
    }

    public static GetPostListResponse from(List<PostResponse> posts, PageInfoResponse pageInfo) {
        return new GetPostListResponse(posts, pageInfo);
    }
}
