package com.gotcha.earlytable.global.error.exception;

import com.gotcha.earlytable.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class UnauthorizedException extends CustomException {

    public UnauthorizedException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
