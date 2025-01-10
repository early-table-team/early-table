package com.gotcha.earlytable.domain.waiting.dto;

import lombok.Getter;

@Getter
public class WaitingNumberResponseDto {

    private final int waitingNumber;

    public WaitingNumberResponseDto(int waitingNumber) {
        this.waitingNumber = waitingNumber;
    }
}
