package com.gotcha.earlytable.domain.waitingsetting.dto;

import com.gotcha.earlytable.domain.waitingsetting.enums.WaitingSettingStatus;
import com.gotcha.earlytable.global.enums.WaitingStatus;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class WaitingSettingRequestDto {

    private final LocalTime waitingOpenTime;

    private final LocalTime waitingClosedTime;

    private final WaitingSettingStatus waitingSettingStatus;

    public WaitingSettingRequestDto(LocalTime waitingOpenTime, LocalTime waitingClosedTime, WaitingSettingStatus waitingSettingStatus) {

        this.waitingOpenTime = waitingOpenTime;
        this.waitingClosedTime = waitingClosedTime;
        this.waitingSettingStatus = waitingSettingStatus;
    }
}
