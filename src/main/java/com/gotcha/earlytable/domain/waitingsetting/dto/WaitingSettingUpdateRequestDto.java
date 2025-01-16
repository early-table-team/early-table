package com.gotcha.earlytable.domain.waitingsetting.dto;

import com.gotcha.earlytable.domain.waitingsetting.enums.WaitingSettingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class WaitingSettingUpdateRequestDto {

    private final LocalTime waitingOpenTime;

    private final LocalTime waitingClosedTime;

    private final WaitingSettingStatus waitingSettingStatus;

    public WaitingSettingUpdateRequestDto(LocalTime waitingOpenTime, LocalTime waitingClosedTime, WaitingSettingStatus waitingSettingStatus) {

        this.waitingOpenTime = waitingOpenTime;
        this.waitingClosedTime = waitingClosedTime;
        this.waitingSettingStatus = waitingSettingStatus;
    }
}
