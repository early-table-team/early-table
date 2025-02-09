package com.gotcha.earlytable.domain.reservation.dto;

import com.gotcha.earlytable.domain.reservation.entity.Reservation;
import lombok.Getter;

@Getter
public class ReservationGetAllResponseDto {

    private final Long storeId;

    private final Long reservationId;

    private final String storeName;

    private final String reservationStatus;

    private final String datetime;

    public ReservationGetAllResponseDto(Reservation reservation) {
        this.storeId = reservation.getStore().getStoreId();
        this.reservationId = reservation.getReservationId();
        this.storeName = reservation.getStore().getStoreName();
        this.reservationStatus = reservation.getReservationStatus().getValue();
        this.datetime = reservation.getReservationDate().toString() + " " + reservation.getReservationTime().toString();

    }

}
