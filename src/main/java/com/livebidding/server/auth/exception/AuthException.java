package com.livebidding.server.auth.exception;

import com.livebidding.server.global.error.BaseException;
import com.livebidding.server.global.error.ErrorCode;

public class AuthException extends BaseException {
    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
