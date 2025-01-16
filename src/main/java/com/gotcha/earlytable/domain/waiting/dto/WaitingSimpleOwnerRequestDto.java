package com.gotcha.earlytable.domain.waiting.dto;

import com.gotcha.earlytable.global.enums.WaitingType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class WaitingSimpleOwnerRequestDto {
    @NotNull
    private final WaitingType waitingType;

    public WaitingSimpleOwnerRequestDto(WaitingType waitingType) {
        this.waitingType = waitingType;
    }
}
