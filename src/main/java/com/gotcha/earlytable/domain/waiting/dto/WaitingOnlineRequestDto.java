package com.gotcha.earlytable.domain.waiting.dto;

import com.gotcha.earlytable.global.enums.WaitingType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class WaitingOnlineRequestDto {
    @Min(1)
    private final int personnelCount;
    @NotNull
    private final WaitingType waitingType;

    public WaitingOnlineRequestDto(int personnelCount, WaitingType waitingType) {
        this.personnelCount = personnelCount;
        this.waitingType = waitingType;
    }
}
