package com.gotcha.earlytable.global.enums;

public enum WaitingStatus {
    PENDING("대기"),
    COMPLETED("입장완료"),
    CANCELED("취소"),
    DELAY("미루기");

    private String value;

    WaitingStatus(String value) {
        this.value = value;
    }
}
