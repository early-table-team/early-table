package com.gotcha.earlytable.domain.reservation.dto;


import lombok.Getter;

import java.time.LocalDate;

@Getter
public class OwnerReservationRequestDto {

    private final LocalDate reservationDate;

    private final Long storeId;

    public OwnerReservationRequestDto(LocalDate reservationDate, Long storeId) {
        this.reservationDate = reservationDate;
        this.storeId = storeId;
    }
}
