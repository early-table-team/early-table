package com.gotcha.earlytable.domain.waitingsetting.enums;

public enum WaitingSettingStatus {
    OPEN("오픈"),
    CLOSE("마감");

    private String value;

    WaitingSettingStatus(String value) {
        this.value = value;
    }
}
