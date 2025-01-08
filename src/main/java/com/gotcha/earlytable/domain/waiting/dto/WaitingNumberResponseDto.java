package com.gotcha.earlytable.domain.waiting.dto;

import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import lombok.Getter;

@Getter
public class WaitingNumberResponseDto {
    private final Long waitingNumber;

    public WaitingNumberResponseDto(Waiting waiting) {
        this.waitingNumber = waiting.getWaitingId();
    }
}
