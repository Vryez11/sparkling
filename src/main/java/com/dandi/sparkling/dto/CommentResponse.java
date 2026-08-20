package com.dandi.sparkling.dto;

import com.dandi.sparkling.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {

    private final Long commentId;
    private final String content;
    private final Long authorId;
    private final String author;
    private final LocalDateTime createdAt;

    private CommentResponse(Long commentId, String content, Long authorId, String author, LocalDateTime createdAt) {
        this.commentId = commentId;
        this.content = content;
        this.authorId = authorId;
        this.author = author;
        this.createdAt = createdAt;
    }

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getId(),
                comment.getUser().getNickname(),
                comment.getCreatedAt()
        );
    }
}
