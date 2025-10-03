package com.livebidding.server.global.error;

import com.livebidding.server.auth.exception.AuthErrorCode;
import com.livebidding.server.product.exception.ProductException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn(">> 비즈니스 예외 발생: {}", e.getMessage());
        return createResponseEntity(errorCode);
    }

    @ExceptionHandler(ProductException.class)
    protected ResponseEntity<ErrorResponse> handleProductException(ProductException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn(">> 상품 관련 예외 발생: {}", e.getMessage());
        return createResponseEntity(errorCode);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorCode errorCode = GlobalErrorCode.INPUT_VALUE_INVALID;
        String detailMessage = createDetailMessage(e, errorCode);
        log.warn(">> 입력값 유효성 검사 실패: {}", detailMessage);
        return createResponseEntity(errorCode, detailMessage);
    }

    private String createDetailMessage(MethodArgumentNotValidException e, ErrorCode defaultErrorCode) {
        List<String> errorMessages = e.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    if (error instanceof FieldError fieldError) {
                        return String.format("'%s' 필드: %s", fieldError.getField(), fieldError.getDefaultMessage());
                    }
                    return error.getDefaultMessage();
                })
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());

        String detailMessage = String.join(", ", errorMessages);
        if (!StringUtils.hasText(detailMessage)) {
            return defaultErrorCode.getMessage();
        }
        return detailMessage;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn(">> 지원하지 않는 HTTP 메소드 요청: {}", e.getMethod());
        return createResponseEntity(GlobalErrorCode.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        log.warn(">> 인증 실패: 로그인이 필요합니다.");
        return createResponseEntity(AuthErrorCode.AUTHENTICATION_REQUIRED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.warn(">> 권한 부족: 접근 권한이 없습니다.");
        return createResponseEntity(AuthErrorCode.ACCESS_DENIED);
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
