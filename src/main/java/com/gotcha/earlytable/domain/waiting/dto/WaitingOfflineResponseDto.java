package com.gotcha.earlytable.domain.waiting.dto;

import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import lombok.Getter;

@Getter
public class WaitingOfflineResponseDto {
    private final Long waitingNumber;

    public WaitingOfflineResponseDto(Waiting waiting) {
        this.waitingNumber = waiting.getWaitingId();
    }
}
