package com.gotcha.earlytable.domain.store.dto;

import com.gotcha.earlytable.domain.store.enums.DayOfWeek;
import com.gotcha.earlytable.domain.store.enums.DayStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class StoreHourRequestDto {

    @NotNull
    private final DayOfWeek dayOfWeek;

    @NotNull
    private final LocalTime openTime;

    @NotNull
    private final LocalTime closedTime;

    @NotNull
    private final DayStatus dayStatus;

    public StoreHourRequestDto(DayOfWeek dayOfWeek, LocalTime openTime, LocalTime closedTime, DayStatus dayStatus) {
        this.dayOfWeek = dayOfWeek;
        this.openTime = openTime;
        this.closedTime = closedTime;
        this.dayStatus = dayStatus;
    }
}
