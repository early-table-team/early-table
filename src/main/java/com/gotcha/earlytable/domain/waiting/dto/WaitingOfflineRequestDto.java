package com.gotcha.earlytable.domain.waiting.dto;

import com.gotcha.earlytable.global.enums.WaitingType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class WaitingOfflineRequestDto {

    @NotNull
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "유효한 핸드폰 번호를 입력하세요.")
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
