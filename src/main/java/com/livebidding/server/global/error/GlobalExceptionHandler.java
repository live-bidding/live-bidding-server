package com.livebidding.server.global.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn(">> 비즈니스 예외 발생: {}", e.getMessage());
        return createResponseEntity(errorCode);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorCode errorCode = GlobalErrorCode.INVALID_INPUT_VALUE;
        String detailMessage = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> String.format("'%s' 필드: %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.joining(", "));
        log.warn(">> 입력값 유효성 검사 실패: {}", detailMessage);
        return createResponseEntity(errorCode, detailMessage);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn(">> 지원하지 않는 HTTP 메소드 요청: {}", e.getMethod());
        return createResponseEntity(GlobalErrorCode.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error(">> 처리되지 않은 예외 발생: {}", e.getMessage(), e);
        return createResponseEntity(GlobalErrorCode.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> createResponseEntity(ErrorCode errorCode) {
        return new ResponseEntity<>(
                new ErrorResponse(errorCode.getStatus().value(), errorCode.getMessage()),
                errorCode.getStatus()
        );
    }

    private ResponseEntity<ErrorResponse> createResponseEntity(ErrorCode errorCode, String message) {
        return new ResponseEntity<>(
                new ErrorResponse(errorCode.getStatus().value(), message),
                errorCode.getStatus()
        );
    }
}
