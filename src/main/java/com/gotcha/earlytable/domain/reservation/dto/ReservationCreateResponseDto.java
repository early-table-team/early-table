package com.gotcha.earlytable.domain.reservation.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

@Getter
public class ReservationCreateResponseDto {

    private final Long reservationId;

    private final LocalDateTime reservationDate;

    private final Integer personnelCount;

    private final List<HashMap<String, Long>> menuList;

    public ReservationCreateResponseDto(Long reservationId, LocalDateTime reservationDate, Integer personnelCount, List<HashMap<String, Long>> menuList) {
        this.reservationId = reservationId;
        this.reservationDate = reservationDate;
        this.personnelCount = personnelCount;
        this.menuList = menuList;
    }
}
