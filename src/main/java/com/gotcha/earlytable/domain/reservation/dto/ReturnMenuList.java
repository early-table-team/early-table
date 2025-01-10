package com.gotcha.earlytable.domain.reservation.dto;

import lombok.Getter;

@Getter
public class ReturnMenuList {
    private final Long menuId;

    private final Long menuCount;

    private final String menuName;

    public ReturnMenuList(Long menuId, Long menuCount, String menuName) {
        this.menuId = menuId;
        this.menuCount = menuCount;
        this.menuName = menuName;
    }

}
