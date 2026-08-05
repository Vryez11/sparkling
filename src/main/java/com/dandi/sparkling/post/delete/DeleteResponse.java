package com.dandi.sparkling.post.delete;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeleteResponse {

    Long postId;

    LocalDateTime deletedAt;

    private DeleteResponse(Long id, LocalDateTime dateTime) {
        this.postId = id;
        this.deletedAt = dateTime;
    }

    public static DeleteResponse from(Long id, LocalDateTime dateTime) {
        return new DeleteResponse(id, dateTime);
    }
}
