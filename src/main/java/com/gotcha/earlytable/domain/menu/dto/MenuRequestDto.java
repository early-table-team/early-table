package com.gotcha.earlytable.domain.menu.dto;

import com.gotcha.earlytable.domain.menu.MenuStatus;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class MenuRequestDto {

    private final String menuName;

    private final String menuContents;

    private final Integer menuPrice;

    private final MenuStatus menuStatus;

    private final MultipartFile image;

    public MenuRequestDto(String menuName, String menuContents, Integer menuPrice, MenuStatus menuStatus, MultipartFile image) {
        this.menuName = menuName;
        this.menuContents = menuContents;
        this.menuPrice = menuPrice;
        this.menuStatus = menuStatus;
        this.image = image;
    }
}
