package com.livebidding.server.user.exception;

import com.livebidding.server.global.error.BaseException;
import com.livebidding.server.global.error.ErrorCode;

public class UserException extends BaseException {
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
