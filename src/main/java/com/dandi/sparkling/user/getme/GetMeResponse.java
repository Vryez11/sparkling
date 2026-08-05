package com.dandi.sparkling.user.getme;

import com.dandi.sparkling.user.share.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetMeResponse {

    private Long id;

    private String nickname;

    private String email;

    private LocalDateTime createdAt;

    public static GetMeResponse from(User user) {

        GetMeResponse response = new GetMeResponse();

        response.setId(user.getId());
        response.setNickname(user.getNickname());
        response.setEmail(user.getEmail());
        response.setCreatedAt(user.getCreatedAt());

        return response;
    }
}
