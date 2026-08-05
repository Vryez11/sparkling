package com.dandi.sparkling.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeletePostResponse {

    Long postId;

    LocalDateTime deletedAt;

    private DeletePostResponse(Long id, LocalDateTime dateTime) {
        this.postId = id;
        this.deletedAt = dateTime;
    }

    public static DeletePostResponse from(Long id, LocalDateTime dateTime) {
        return new DeletePostResponse(id, dateTime);
    }
}
