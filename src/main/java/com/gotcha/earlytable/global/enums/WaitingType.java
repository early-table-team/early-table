package com.gotcha.earlytable.global.enums;

public enum WaitingType {

    DINE("매장"),
    TOGO("포장");

    private String value;

    WaitingType(String value) {
        this.value = value;
    }
}
