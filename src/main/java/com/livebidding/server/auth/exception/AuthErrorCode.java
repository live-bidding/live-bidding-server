package com.livebidding.server.auth.exception;

import com.livebidding.server.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    INVALID_JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, "JWT 서명이 유효하지 않습니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "JWT 토큰이 만료되었습니다."),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다."),
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다.");

    private static final String PREFIX = "[AUTH ERROR] ";
    private final HttpStatus status;
    private final String rawMessage;

    @Override
    public String getMessage() {
        return PREFIX + rawMessage;
    }
}
