package com.dandi.sparkling.post.create;

import lombok.Data;

@Data
public class CreateResponse {

    Long postId;

    private CreateResponse(Long postId) {
        this.postId = postId;
    }

    public static CreateResponse from(Long postId) {
        return new CreateResponse(postId);
    }
}
