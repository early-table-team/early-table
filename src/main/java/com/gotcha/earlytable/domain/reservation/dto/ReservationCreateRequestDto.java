package com.gotcha.earlytable.domain.reservation.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ReservationCreateRequestDto {

    private final LocalDateTime reservationDate;

    private final Integer personnelCount;

    private final List<HashMap<String, Long>> menuList;

    public ReservationCreateRequestDto(LocalDateTime reservationDate, Integer personnelCount, List<HashMap<String, Long>> menuList) {
        this.reservationDate = reservationDate;
        this.personnelCount = personnelCount;
        this.menuList = menuList;
    }


}
