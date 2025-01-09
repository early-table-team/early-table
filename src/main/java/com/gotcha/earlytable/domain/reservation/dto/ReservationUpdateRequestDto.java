package com.gotcha.earlytable.domain.reservation.dto;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;

@Getter
public class ReservationUpdateRequestDto {

    private final List<HashMap<String, Long>> menuList;

    public ReservationUpdateRequestDto(List<HashMap<String, Long>> menuList) {
        this.menuList = menuList;
    }

}
