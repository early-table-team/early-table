package com.gotcha.earlytable.domain.menu;

import lombok.Getter;

@Getter
public enum MenuStatus {
    RECOMMENDED("추천"),
    NORMAL("일반");

    private final String menuStatusName;

    MenuStatus(String menuStatusName) {
        this.menuStatusName = menuStatusName;
    }

}
