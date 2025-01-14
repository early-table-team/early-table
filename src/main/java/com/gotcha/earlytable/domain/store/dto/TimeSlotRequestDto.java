package com.gotcha.earlytable.domain.store.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class TimeSlotRequestDto {

    @NotNull
    private final  LocalTime reservationTime;

    public TimeSlotRequestDto(LocalTime reservationTime) {
        this.reservationTime = reservationTime;
    }
}
