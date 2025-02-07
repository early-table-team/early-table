package com.gotcha.earlytable.domain.waiting.dto;

import com.gotcha.earlytable.global.enums.WaitingType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class WaitingOwnerRequestDto {
    @NotNull
    private final WaitingType waitingType;
    @NotNull
    private final LocalDate date;

    public WaitingOwnerRequestDto(WaitingType waitingType, LocalDate date) {
        this.waitingType = waitingType;
        this.date = date;
    }
}
