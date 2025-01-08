package com.gotcha.earlytable.domain.waiting.dto;

import com.gotcha.earlytable.global.enums.WaitingType;
import lombok.Getter;

@Getter
public class WaitingOnlineRequestDto {
    private final int personnelCount;
    private final WaitingType waitingType;

    public WaitingOnlineRequestDto(int personnelCount, WaitingType waitingType) {
        this.personnelCount = personnelCount;
        this.waitingType = waitingType;
    }
}
