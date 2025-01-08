package com.gotcha.earlytable.domain.store.dto;

import com.gotcha.earlytable.domain.store.entity.StoreHour;
import com.gotcha.earlytable.domain.store.enums.DayOfWeek;
import com.gotcha.earlytable.domain.store.enums.DayStatus;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class StoreHourResponseDto {

    private final Long storeId;

    private final Long hourId;

    private final DayOfWeek dayOfWeek;

    private final LocalTime openTime;

    private final LocalTime closedTime;

    private final DayStatus dayStatus;


    public StoreHourResponseDto(Long storeId, Long hourId, DayOfWeek dayOfWeek, LocalTime openTime, LocalTime closedTime, DayStatus dayStatus) {
        this.storeId = storeId;
        this.hourId = hourId;
        this.dayOfWeek = dayOfWeek;
        this.openTime = openTime;
        this.closedTime = closedTime;
        this.dayStatus = dayStatus;
    }

    public static StoreHourResponseDto toDto(StoreHour storeHour) {
        return new StoreHourResponseDto(
                storeHour.getStore().getStoreId(),
                storeHour.getStoreHourId(),
                storeHour.getDayOfWeek(),
                storeHour.getOpenTime(),
                storeHour.getClosedTime(),
                storeHour.getDayStatus()
        );
    }
}
