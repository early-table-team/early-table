package com.gotcha.earlytable.domain.waitingsetting.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.gotcha.earlytable.domain.waitingsetting.entity.WaitingSetting;
import com.gotcha.earlytable.domain.waitingsetting.enums.WaitingSettingStatus;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class WaitingSettingUpdateStatusResponseDto {

    private final WaitingSettingStatus waitingSettingStatus;

    @JsonCreator
    public WaitingSettingUpdateStatusResponseDto(WaitingSettingStatus waitingSettingStatus) {
        this.waitingSettingStatus = waitingSettingStatus;
    }

    public static WaitingSettingUpdateStatusResponseDto toDto(WaitingSetting waitingSetting) {
        return new WaitingSettingUpdateStatusResponseDto(
                waitingSetting.getWaitingSettingStatus()
        );
    }
}
