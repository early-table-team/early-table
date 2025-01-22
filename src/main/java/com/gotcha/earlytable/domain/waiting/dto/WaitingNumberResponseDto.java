package com.gotcha.earlytable.domain.waiting.dto;

import lombok.Getter;

@Getter
public class WaitingNumberResponseDto {

    private final Long waitingNumber;

    public WaitingNumberResponseDto(Long waitingNumber) {
        this.waitingNumber = waitingNumber;
    }
}
