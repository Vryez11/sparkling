package com.dandi.sparkling.dto;

import com.dandi.sparkling.entity.User;
import lombok.Data;

@Data
public class LoginResponse {

    private String accessToken;

    private String refreshToken;

    private Long userId;

    private String email;

    public static LoginResponse from (User user, String accessToken, String refreshToken) {

        LoginResponse response = new LoginResponse();

        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());

        return response;
    }
}
