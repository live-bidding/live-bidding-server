package com.livebidding.server.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {

	INPUT_VALUE_INVALID(HttpStatus.BAD_REQUEST, "G001", "[ERROR] 유효하지 않은 입력 값입니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G002", "[ERROR] 서버 내부 오류입니다."),
	UNSUPPORTED_HASH_ALGORITHM(HttpStatus.INTERNAL_SERVER_ERROR, "G003", "[ERROR] 지원하지 않는 해시 알고리즘입니다."),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "G004", "[ERROR] 허용되지 않은 메소드입니다."),
	;

	private final HttpStatus status;
	private final String code;
	private final String message;

}