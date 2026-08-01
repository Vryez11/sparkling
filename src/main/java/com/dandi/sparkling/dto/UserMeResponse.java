package com.dandi.sparkling.dto;

import com.dandi.sparkling.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserMeResponse {

    private Long id;

    private String nickname;

    private String email;

    private LocalDateTime createdAt;

    public static UserMeResponse from(User user) {

        UserMeResponse response = new UserMeResponse();

        response.setId(user.getId());
        response.setNickname(user.getNickname());
        response.setEmail(user.getEmail());
        response.setCreatedAt(user.getCreatedAt());

        return response;
    }
}
