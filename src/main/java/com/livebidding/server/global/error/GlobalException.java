package com.livebidding.server.global.error;

public class GlobalException extends BaseException {
    public GlobalException(ErrorCode errorCode) {
        super(errorCode);
    }

    public GlobalException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
