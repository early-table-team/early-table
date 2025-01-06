package com.gotcha.earlytable.global.error.exception;

import com.gotcha.earlytable.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class ConflictException extends CustomException {

    public ConflictException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
