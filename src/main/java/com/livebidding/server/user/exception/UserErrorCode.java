package com.livebidding.server.user.exception;

import com.livebidding.server.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "유효하지 않은 이메일 형식입니다."),
    INVALID_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST, "비밀번호가 유효한 BCrypt 형식이 아닙니다."),
    EMPTY_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호는 공백일 수 없습니다."),
    EMPTY_USERNAME(HttpStatus.BAD_REQUEST, "사용자 이름은 공백일 수 없습니다.");

    private static final String PREFIX = "[USER ERROR] ";

    private final HttpStatus status;
    private final String rawMessage;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return PREFIX + rawMessage;
    }
}
