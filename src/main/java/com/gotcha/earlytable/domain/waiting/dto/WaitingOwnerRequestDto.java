package com.gotcha.earlytable.domain.waiting.dto;

import com.gotcha.earlytable.global.enums.WaitingType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class WaitingOwnerRequestDto {
    @NotNull
    private final WaitingType waitingType;

    public WaitingOwnerRequestDto(WaitingType waitingType) {
        this.waitingType = waitingType;
    }
}
