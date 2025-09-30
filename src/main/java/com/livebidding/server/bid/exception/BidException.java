package com.livebidding.server.bid.exception;

import com.livebidding.server.global.error.BaseException;
import com.livebidding.server.global.error.ErrorCode;

public class BidException extends BaseException {
    public BidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
