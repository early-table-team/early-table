package com.gotcha.earlytable.global.enums;

import lombok.Getter;

@Getter
public enum ReservationStatus {

    PENDING("예정"),
    COMPLETED("완료"),
    CANCELED("취소");

    private final String value;

    ReservationStatus(String value) {
        this.value = value;
    }

}
