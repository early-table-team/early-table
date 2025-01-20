package com.gotcha.earlytable.global.enums;

import lombok.Getter;

@Getter
public enum InvitationStatus {

    ACCEPTED("수락"),
    PENDING("대기"),
    REJECTED("거절"),
    CANCELED("취소"),
    LEAVED("탈퇴"),
    EXILE("추방");

    private final String value;

    InvitationStatus(String value) {
        this.value = value;
    }
}