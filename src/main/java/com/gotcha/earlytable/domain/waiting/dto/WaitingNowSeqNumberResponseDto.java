package com.gotcha.earlytable.domain.waiting.dto;

import lombok.Getter;

@Getter
public class WaitingNowSeqNumberResponseDto {

    private final Integer waitingNowSeqNumber;

    public WaitingNowSeqNumberResponseDto(Integer waitingNowSeqNumber) {
        this.waitingNowSeqNumber = waitingNowSeqNumber;
    }
}
