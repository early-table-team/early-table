package com.gotcha.earlytable.global.error.exception;

import com.gotcha.earlytable.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundException extends CustomException {

    public NotFoundException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
