package com.gotcha.earlytable.domain.waitingsetting.dto;

import com.gotcha.earlytable.domain.waitingsetting.entity.WaitingSetting;
import com.gotcha.earlytable.domain.waitingsetting.enums.WaitingSettingStatus;
import com.gotcha.earlytable.global.enums.WaitingStatus;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class WaitingSettingResponseDto {

    private final Long waitingSettingId;

    private final Long storeId;

    private final LocalTime waitingOpenTime;

    private final LocalTime waitingClosedTime;

    private final WaitingSettingStatus waitingSettingStatus;

    public WaitingSettingResponseDto(Long waitingSettingId, Long storeId, LocalTime waitingOpenTime,
                                     LocalTime waitingClosedTime, WaitingSettingStatus waitingSettingStatus) {

        this.waitingSettingId = waitingSettingId;
        this.storeId = storeId;
        this.waitingOpenTime = waitingOpenTime;
        this.waitingClosedTime = waitingClosedTime;
        this.waitingSettingStatus = waitingSettingStatus;
    }


    public static WaitingSettingResponseDto toDto(WaitingSetting waitingSetting) {
        return new WaitingSettingResponseDto(
                waitingSetting.getWaitingSettingId(),
                waitingSetting.getStore().getStoreId(),
                waitingSetting.getWaitingOpenTime(),
                waitingSetting.getWaitingClosedTime(),
                waitingSetting.getWaitingSettingStatus()
        );
    }
}
