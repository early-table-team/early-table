package com.gotcha.earlytable.domain.menu.dto;

import com.gotcha.earlytable.domain.menu.MenuStatus;
import com.gotcha.earlytable.domain.menu.entity.Menu;
import lombok.Getter;

@Getter
public class MenuResponseDto {
    private String menuName;
    private String menuContents;
    private Integer menuPrice;
    private MenuStatus menuStatus;

    public MenuResponseDto(String menuName, String menuContents, Integer menuPrice, MenuStatus menuStatus) {
        this.menuName = menuName;
        this.menuContents = menuContents;
        this.menuPrice = menuPrice;
        this.menuStatus = menuStatus;
    }

    public static MenuResponseDto toDto(Menu menu) {
        return new MenuResponseDto(
                menu.getMenuName(),
                menu.getMenuContents(),
                menu.getMenuPrice(),
                menu.getMenuStatus()
        );
    }
}
