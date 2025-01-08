package com.gotcha.earlytable.domain.store.dto;

import com.gotcha.earlytable.domain.store.enums.DayOfWeek;
import com.gotcha.earlytable.domain.store.enums.DayStatus;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class StoreHourRequestDto {

    private final DayOfWeek dayOfWeek;

    private final LocalTime openTime;

    private final LocalTime closedTime;

    private final DayStatus dayStatus;

    public StoreHourRequestDto(DayOfWeek dayOfWeek, LocalTime openTime, LocalTime closedTime, DayStatus dayStatus) {
        this.dayOfWeek = dayOfWeek;
        this.openTime = openTime;
        this.closedTime = closedTime;
        this.dayStatus = dayStatus;
    }
}
