package com.gotcha.earlytable.domain.waiting.dto;

import com.gotcha.earlytable.global.enums.WaitingType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class WaitingOnlineRequestDto {
    @NotNull
    private final int personnelCount;
    @NotNull
    private final WaitingType waitingType;

    public WaitingOnlineRequestDto(int personnelCount, WaitingType waitingType) {
        this.personnelCount = personnelCount;
        this.waitingType = waitingType;
    }
}
