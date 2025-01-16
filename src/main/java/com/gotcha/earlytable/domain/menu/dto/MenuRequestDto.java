package com.gotcha.earlytable.domain.menu.dto;

import com.gotcha.earlytable.domain.menu.MenuStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class MenuRequestDto {

    @NotNull
    private final String menuName;

    private final String menuContents;

    @NotNull
    @Min(value = 1000, message = "메뉴 최소 금액은 1000원입니다.")
    private final Integer menuPrice;

    @NotNull
    @Enumerated(EnumType.STRING)
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
