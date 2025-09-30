package com.livebidding.server.bid.exception;

import com.livebidding.server.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BidErrorCode implements ErrorCode {
    BIDDER_CANNOT_BE_NULL(HttpStatus.BAD_REQUEST, "입찰자는 null일 수 없습니다."),
    PRODUCT_CANNOT_BE_NULL(HttpStatus.BAD_REQUEST, "입찰 상품은 null일 수 없습니다.");

    private static final String PREFIX = "[BID ERROR] ";

    private final HttpStatus status;
    private final String rawMessage;

    @Override
    public String getMessage() {
        return PREFIX + rawMessage;
    }
}
