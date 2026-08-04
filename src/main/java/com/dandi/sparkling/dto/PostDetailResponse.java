package com.dandi.sparkling.dto;

import com.dandi.sparkling.entity.Post;
import com.dandi.sparkling.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDetailResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String nickname;
    private final LocalDateTime createdAt;

    private PostDetailResponse(Long id, String title, String content, String nickname, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.createdAt = createdAt;
    }

    public static PostDetailResponse from(Post post) {
        return new PostDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getNickname(),
                post.getCreatedAt()
        );
    }
}
