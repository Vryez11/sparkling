package com.dandi.sparkling.post.getlist;

import lombok.Data;

import java.util.List;

@Data
public class GetListResponse {

    List<PostSummary> posts;

    private GetListResponse(List<PostSummary> posts) {
        this.posts = posts;
    }

    public static GetListResponse from(List<PostSummary> posts) {

        return new GetListResponse(posts);
    }
}
