package com.dandi.sparkling.dto;

import lombok.Data;

@Data
public class RefreshResponse {

    private String accessToken;

    private String refreshToken;

    public static RefreshResponse of(String accessToken, String refreshToken) {

        RefreshResponse response = new RefreshResponse();

        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);

        return response;
    }
}
