package com.dandi.sparkling.auth.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @Size(max = 100)
    @NotBlank
    private String email;

    @Size(min = 8, max = 64)
    @NotBlank
    private String password;
}
