package com.gotcha.earlytable.domain.store.dto;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class TimeSlotRequestDto {

    private final  LocalTime reservationTime;

    public TimeSlotRequestDto(LocalTime reservationTime) {
        this.reservationTime = reservationTime;
    }
}
