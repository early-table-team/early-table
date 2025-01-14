package com.gotcha.earlytable.domain.menu.dto;

import com.gotcha.earlytable.domain.menu.MenuStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class MenuRequestDto {

    @NotEmpty(message = "메뉴 이름을 입력해주세요 ")
    private final String menuName;

    @NotEmpty(message = "메뉴 설명을 입력해주세요 ")
    private final String menuContents;

    @Min(value = 1000, message = "메뉴 최소 금액은 1000원입니다.")
    private final Integer menuPrice;

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
