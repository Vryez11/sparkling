package com.dandi.sparkling.post.getlist;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostSummary {

    private Long postId;
    private String title;
    private String nickname;
    private LocalDateTime createAt;

    public static PostSummary from(Long postId, String title, String nickname, LocalDateTime dateTime) {

        PostSummary postSummary = new PostSummary();

        postSummary.setPostId(postId);
        postSummary.setTitle(title);
        postSummary.setNickname(nickname);
        postSummary.setCreateAt(dateTime);

        return postSummary;
    }
}
