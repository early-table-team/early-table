package com.gotcha.earlytable.domain.store.dto;

import com.gotcha.earlytable.domain.store.enums.DayStatus;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class StoreHourUpdateRequestDto {

    private final LocalTime openTime;

    private final LocalTime closedTime;

    private final DayStatus dayStatus;

    public StoreHourUpdateRequestDto(LocalTime openTime, LocalTime closedTime, DayStatus dayStatus) {
        this.openTime = openTime;
        this.closedTime = closedTime;
        this.dayStatus = dayStatus;
    }
}
