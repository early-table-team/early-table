package com.gotcha.earlytable.domain.reservation.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

@Getter
public class ReservationCreateResponseDto {

    private final Long reservationId;

    private final LocalDate reservationDate;

    private final LocalTime reservationTime;

    private final Integer personnelCount;

    private final List<ReturnMenuListDto> menuList;

    public ReservationCreateResponseDto(Long reservationId, LocalDate reservation, LocalTime reservationTime, Integer personnelCount, List<ReturnMenuListDto> menuList) {
        this.reservationId = reservationId;
        this.reservationDate = reservation;
        this.reservationTime = reservationTime;
        this.personnelCount = personnelCount;
        this.menuList = menuList;
    }
}
