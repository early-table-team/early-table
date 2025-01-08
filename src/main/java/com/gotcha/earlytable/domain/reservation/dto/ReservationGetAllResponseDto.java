package com.gotcha.earlytable.domain.reservation.dto;

import com.gotcha.earlytable.domain.reservation.entity.Reservation;
import com.gotcha.earlytable.global.enums.ReservationStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReservationGetAllResponseDto {

    private final Long storeId;

    private final Long reservationId;

    private final LocalDateTime reservationDate;

    private final String storeName;

    private final ReservationStatus reservationStatus;

    public ReservationGetAllResponseDto(Reservation reservation) {
        this.storeId = reservation.getReservationId();
        this.reservationId = reservation.getReservationId();
        this.reservationDate = reservation.getReservationDateTime();
        this.storeName = reservation.getStore().getStoreName();
        this.reservationStatus = reservation.getReservationStatus();
    }

}
