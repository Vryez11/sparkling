package com.dandi.sparkling.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponse {

    private Long postId;
    private String title;
    private String nickname;
    private LocalDateTime createAt;

    public static PostResponse from(Long postId, String title, String nickname, LocalDateTime dateTime) {

        PostResponse postResponse = new PostResponse();

        postResponse.setPostId(postId);
        postResponse.setTitle(title);
        postResponse.setNickname(nickname);
        postResponse.setCreateAt(dateTime);

        return postResponse;
    }
}
