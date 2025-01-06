package com.gotcha.earlytable.domain.store.enums;

import lombok.Getter;

@Getter
public enum ReservationType {
    RESERVATION("예약"),
    ONSITE("현장"),
    REMOTE("원격");

    private final String TypeName;

    ReservationType(String typeName) {
        this.TypeName = typeName;
    }
}
