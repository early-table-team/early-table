package com.gotcha.earlytable.domain.store.dto;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class TimeSlotResponseDto {

    private final Long timeSlotId;

    private final LocalTime reservationTime;

    public TimeSlotResponseDto(Long timeSlotId, LocalTime reservationTime) {
        this.timeSlotId = timeSlotId;
        this.reservationTime = reservationTime;
    }

}
