package com.gotcha.earlytable.global.error.exception;

import com.gotcha.earlytable.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class BadRequestException extends CustomException {

    public BadRequestException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
