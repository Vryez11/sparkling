package com.dandi.sparkling.dto;

import com.dandi.sparkling.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostDetailResponse {

    private final Long postId;
    private final String title;
    private final String content;
    private final String nickname;
    private final int likeCount;
    private final LocalDateTime createdAt;

    private PostDetailResponse(Long postId, String title, String content, String nickname, int likeCount, LocalDateTime createdAt) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
    }

    public static PostDetailResponse from(Post post) {
        return new PostDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getNickname(),
                post.getLikeCount(),
                post.getCreatedAt()
        );
    }
}
