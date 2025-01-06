package com.gotcha.earlytable.global.enums;

public enum ReservationStatus {

    PENDING("예정"),
    COMPLETED("완료"),
    CANCELED("취소");

    private String value;

    ReservationStatus(String value) {
        this.value = value;
    }

}
