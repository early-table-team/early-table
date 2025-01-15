package com.gotcha.earlytable.domain.waitingsetting.dto;

import com.gotcha.earlytable.domain.waitingsetting.enums.WaitingSettingStatus;
import com.gotcha.earlytable.global.enums.WaitingStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class WaitingSettingRequestDto {
    @NotNull
    private final LocalTime waitingOpenTime;

    @NotNull
    private final LocalTime waitingClosedTime;

    @NotNull
    private final WaitingSettingStatus waitingSettingStatus;

    public WaitingSettingRequestDto(LocalTime waitingOpenTime, LocalTime waitingClosedTime, WaitingSettingStatus waitingSettingStatus) {

        this.waitingOpenTime = waitingOpenTime;
        this.waitingClosedTime = waitingClosedTime;
        this.waitingSettingStatus = waitingSettingStatus;
    }
}
