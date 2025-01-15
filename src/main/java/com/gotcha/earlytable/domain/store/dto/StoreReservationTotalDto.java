package com.gotcha.earlytable.domain.store.dto;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class StoreReservationTotalDto {

    private final LocalTime reservationTime;    // 타임 슬롯

    private final Integer tableMaxNumber;       // 최대 몇 인석

    private final Integer tableMinNumber;       // 최소 몇 인석

    private final Integer remainTableCount;     // 잔여 개수

    public StoreReservationTotalDto(LocalTime reservationTime, Integer tableMaxNumber, Integer tableMinNumber, Integer remainTableCount) {
        this.reservationTime = reservationTime;
        this.tableMaxNumber = tableMaxNumber;
        this.tableMinNumber = tableMinNumber;
        this.remainTableCount = remainTableCount;
    }
}
