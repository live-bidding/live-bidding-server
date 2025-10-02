package com.livebidding.server.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {

	INPUT_VALUE_INVALID(HttpStatus.BAD_REQUEST, "유효하지 않은 입력 값입니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
	UNSUPPORTED_HASH_ALGORITHM(HttpStatus.INTERNAL_SERVER_ERROR, "지원하지 않는 해시 알고리즘입니다."),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메소드입니다."),
	PAGE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "요청할 수 있는 페이지 크기를 초과했습니다."),
	;

	private static final String PREFIX = "[SYSTEM ERROR] ";
	private final HttpStatus status;
	private final String rawMessage;

	@Override
	public String getMessage() {
		return PREFIX + rawMessage;
	}

}
