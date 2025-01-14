package com.gotcha.earlytable.domain.waiting.dto;

import com.gotcha.earlytable.global.enums.WaitingType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class WaitingOfflineRequestDto {

    @NotNull
    private String phoneNumber;
    @NotNull
    private int personnelCount;
    @NotNull
    private WaitingType waitingType;

    WaitingOfflineRequestDto(String phoneNumber, int personnelCount, WaitingType waitingType) {
        this.phoneNumber = phoneNumber;
        this.personnelCount = personnelCount;
        this.waitingType = waitingType;
    }
}
