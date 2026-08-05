package com.dandi.sparkling.post.getdetail;

import com.dandi.sparkling.post.share.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetDetailResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String nickname;
    private final LocalDateTime createdAt;

    private GetDetailResponse(Long id, String title, String content, String nickname, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.createdAt = createdAt;
    }

    public static GetDetailResponse from(Post post) {
        return new GetDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getNickname(),
                post.getCreatedAt()
        );
    }
}
