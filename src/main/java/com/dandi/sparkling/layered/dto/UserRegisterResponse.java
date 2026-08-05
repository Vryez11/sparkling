package com.dandi.sparkling.layered.dto;

import com.dandi.sparkling.user.share.User;
import lombok.Data;

@Data
public class UserRegisterResponse {

    private Long id;

    private String nickname;

    private String email;

    public static UserRegisterResponse from (User user) {

        UserRegisterResponse response = new UserRegisterResponse();

        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setNickname(user.getNickname());

        return response;
    }
}
