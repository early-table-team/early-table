package com.gotcha.earlytable.domain.reservation.dto;

import com.gotcha.earlytable.global.enums.ReservationStatus;
import lombok.Getter;

@Getter
public class ReservationUpdateStatusRequestDto {
    private final ReservationStatus reservationStatus;

    public ReservationUpdateStatusRequestDto(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }
}
