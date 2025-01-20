package com.gotcha.earlytable.global.enums;

import lombok.Getter;

@Getter
public enum NotificationType {

    INIT("초기화"),
    STORE_VIEW("가게조회"),
    PARTY("일행"),
    RESERVATION("예약"),
    WAITING("웨이팅"),
    FRIEND("친구"),
    REVIEW("리뷰"),
    NOTICE("공지사항"),
    MESSAGE("메시지");

    private final String value;

    NotificationType(String value) {
        this.value = value;
    }
}
