package com.gotcha.earlytable.global.enums;

public enum WaitingType {

    DINE_IN("매장"),
    TO_GO("포장");

    private String value;

    WaitingType(String value) {
        this.value = value;
    }
}
