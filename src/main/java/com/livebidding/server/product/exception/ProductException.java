package com.livebidding.server.product.exception;

import com.livebidding.server.global.error.BaseException;
import com.livebidding.server.global.error.ErrorCode;

public class ProductException extends BaseException {
    public ProductException(ErrorCode errorCode) {
        super(errorCode);
    }
}
