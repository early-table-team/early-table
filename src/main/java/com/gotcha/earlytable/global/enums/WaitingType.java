package com.gotcha.earlytable.global.enums;

import lombok.Getter;

@Getter
public enum WaitingType {

    DINE_IN("매장"),
    TO_GO("포장");

    private final String value;

    WaitingType(String value) {
        this.value = value;
    }
}
