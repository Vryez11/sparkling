package com.dandi.sparkling.post.update;

import com.dandi.sparkling.post.share.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String nickname;
    private final LocalDateTime createdAt;

    private UpdateResponse(Long id, String title, String content, String nickname, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.createdAt = createdAt;
    }

    public static UpdateResponse from(Post post) {
        return new UpdateResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getNickname(),
                post.getCreatedAt()
        );
    }
}
