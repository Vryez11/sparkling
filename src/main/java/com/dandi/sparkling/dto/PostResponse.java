package com.dandi.sparkling.dto;

import com.dandi.sparkling.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponse {

    private final Long postId;
    private final String title;
    private final String nickname;
    private final LocalDateTime createdAt;

    private PostResponse(Long postId, String title, String nickname, LocalDateTime createdAt) {
        this.postId = postId;
        this.title = title;
        this.nickname = nickname;
        this.createdAt = createdAt;
    }

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getUser().getNickname(),
                post.getCreatedAt()
        );
    }
}
