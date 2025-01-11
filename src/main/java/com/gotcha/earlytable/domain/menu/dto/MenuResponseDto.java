package com.gotcha.earlytable.domain.menu.dto;

import com.gotcha.earlytable.domain.file.entity.FileDetail;
import com.gotcha.earlytable.domain.menu.MenuStatus;
import com.gotcha.earlytable.domain.menu.entity.Menu;
import lombok.Getter;

@Getter
public class MenuResponseDto {

    private final String menuName;

    private final String menuContents;

    private final Integer menuPrice;

    private final MenuStatus menuStatus;

    private final String menuImageUrl;

    public MenuResponseDto(String menuName, String menuContents, Integer menuPrice,
                           MenuStatus menuStatus, String menuImageUrl) {

        this.menuName = menuName;
        this.menuContents = menuContents;
        this.menuPrice = menuPrice;
        this.menuStatus = menuStatus;
        this.menuImageUrl = menuImageUrl;
    }

    public static MenuResponseDto toDto(Menu menu, String menuImageUrl) {
        return new MenuResponseDto(
                menu.getMenuName(),
                menu.getMenuContents(),
                menu.getMenuPrice(),
                menu.getMenuStatus(),
                menuImageUrl
        );
    }

    public static MenuResponseDto toDto(Menu menu) {

        String imageUrl = menu.getFile().getFileDetailList().stream()
                .findAny().map(FileDetail::getFileUrl).orElse(null);

        return new MenuResponseDto(
                menu.getMenuName(),
                menu.getMenuContents(),
                menu.getMenuPrice(),
                menu.getMenuStatus(),
                imageUrl
        );
    }
}
