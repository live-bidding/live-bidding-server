package com.livebidding.server.product.exception;

import com.livebidding.server.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductErrorCode implements ErrorCode {
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    INVALID_AUCTION_TIME(HttpStatus.BAD_REQUEST, "경매 시간이 유효하지 않습니다."),
    INVALID_PRICE(HttpStatus.BAD_REQUEST, "상품 가격이 유효하지 않습니다."),
    BLANK_PRODUCT_NAME(HttpStatus.BAD_REQUEST, "상품명은 비워둘 수 없습니다."),
    PRICE_CANNOT_BE_NULL(HttpStatus.BAD_REQUEST, "상품 가격은 비워둘 수 없습니다."),
    PRICE_CANNOT_BE_NEGATIVE(HttpStatus.BAD_REQUEST, "상품 가격은 음수일 수 없습니다."),
    AUCTION_TIME_CANNOT_BE_NULL(HttpStatus.BAD_REQUEST, "경매 시간은 비워둘 수 없습니다."),
    INVALID_AUCTION_PERIOD(HttpStatus.BAD_REQUEST, "경매 종료 시간은 시작 시간보다 이후여야 합니다."),
    SELLER_CANNOT_BE_NULL(HttpStatus.BAD_REQUEST, "판매자 정보는 비워둘 수 없습니다."),
    UNSUPPORTED_AUCTION_STATUS(HttpStatus.BAD_REQUEST, "지원하지 않는 경매 상태입니다.");

    private static final String PREFIX = "[PRODUCT ERROR] ";
    private final HttpStatus status;
    private final String rawMessage;

    @Override
    public String getMessage() {
        return PREFIX + rawMessage;
    }
}
