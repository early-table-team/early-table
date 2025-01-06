package com.gotcha.earlytable.global.enums;

public enum NotificationType {

    RESERVATION("예약"),
    WAITING("웨이팅"),
    FRIEND("친구"),
    REVIEW("리뷰"),
    NOTICE("공지사항"),
    MESSAGE("메시지");

    private String value;

    NotificationType(String value) {
        this.value = value;
    }
}
