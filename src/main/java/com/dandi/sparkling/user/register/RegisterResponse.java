package com.dandi.sparkling.user.register;

import com.dandi.sparkling.user.share.User;
import lombok.Data;

@Data
public class RegisterResponse {

    private Long id;

    private String nickname;

    private String email;

    public static RegisterResponse from(User user) {

        RegisterResponse response = new RegisterResponse();

        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setNickname(user.getNickname());

        return response;
    }
}
