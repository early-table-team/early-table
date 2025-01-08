package com.gotcha.earlytable.domain.waiting.dto;

import com.gotcha.earlytable.global.enums.WaitingType;
import lombok.Getter;

@Getter
public class WaitingOfflineRequestDto {

    private String phoneNumber;
    private int personnelCount;
    private WaitingType waitingType;

    WaitingOfflineRequestDto(String phoneNumber, int personnelCount, WaitingType waitingType) {
        this.phoneNumber = phoneNumber;
        this.personnelCount = personnelCount;
        this.waitingType = waitingType;
    }
}
