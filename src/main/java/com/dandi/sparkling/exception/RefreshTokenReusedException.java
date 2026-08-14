package com.dandi.sparkling.exception;

public class RefreshTokenReusedException extends RuntimeException {

    public RefreshTokenReusedException() {
        super("유효하지 않은 리프레시 토큰입니다.");
    }
}
