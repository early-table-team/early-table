package com.gotcha.earlytable.domain.waiting.dto;

import lombok.Getter;

@Getter
public class WaitingNumberResponseDto {

    private final Long waitingId;
    private final Long waitingNumber;
    private final String waitingTime;

    public WaitingNumberResponseDto(Long waitingId, Long waitingNumber, Integer waitingTime) {
        this.waitingId = waitingId;
        this.waitingNumber = waitingNumber;
        if (waitingTime <= 5) {
            this.waitingTime = "곧 입장 가능합니다.";
        } else {
            this.waitingTime = String.valueOf(waitingTime);
        }
    }
}
