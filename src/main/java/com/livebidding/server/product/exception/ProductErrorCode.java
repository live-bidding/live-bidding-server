package com.livebidding.server.product.exception;

import com.livebidding.server.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductErrorCode implements ErrorCode {
    PRICE_CANNOT_BE_NULL(HttpStatus.BAD_REQUEST, "가격은 null일 수 없습니다."),
    EMPTY_PRODUCT_NAME(HttpStatus.BAD_REQUEST, "상품명은 공백일 수 없습니다."),
    EMPTY_PRODUCT_DESCRIPTION(HttpStatus.BAD_REQUEST, "상품 설명은 공백일 수 없습니다."),
    PRICE_CANNOT_BE_NEGATIVE(HttpStatus.BAD_REQUEST, "가격은 0 이상이어야 합니다."),
    AUCTION_TIME_CANNOT_BE_NULL(HttpStatus.BAD_REQUEST, "경매 시간은 null일 수 없습니다."),
    INVALID_AUCTION_PERIOD(HttpStatus.BAD_REQUEST, "경매 종료 시간은 시작 시간보다 이후여야 합니다."),
    SELLER_CANNOT_BE_NULL(HttpStatus.BAD_REQUEST, "상품 판매자는 null일 수 없습니다.");

    private static final String PREFIX = "[PRODUCT ERROR] ";

    private final HttpStatus status;
    private final String rawMessage;

    @Override
    public String getMessage() {
        return PREFIX + rawMessage;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}
