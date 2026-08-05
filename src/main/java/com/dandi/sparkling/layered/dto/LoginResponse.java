package com.dandi.sparkling.layered.dto;

import com.dandi.sparkling.user.share.User;
import lombok.Data;

@Data
public class LoginResponse {

    private String accessToken;

    private Long userId;

    private String email;

    public static LoginResponse from (User user, String accessToken) {

        LoginResponse response = new LoginResponse();

        response.setAccessToken(accessToken);
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());

        return response;
    }
}
