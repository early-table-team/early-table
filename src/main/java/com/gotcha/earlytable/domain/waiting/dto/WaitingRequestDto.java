package com.gotcha.earlytable.domain.waiting.dto;

import com.gotcha.earlytable.global.enums.WaitingType;
import lombok.Getter;

@Getter
public class WaitingRequestDto {
    private final int personnelCount;
    private final WaitingType waitingType;

    public WaitingRequestDto(int personnelCount, WaitingType waitingType) {
        this.personnelCount = personnelCount;
        this.waitingType = waitingType;
    }
}
