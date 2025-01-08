package com.gotcha.earlytable.domain.menu.dto;

import com.gotcha.earlytable.domain.menu.MenuStatus;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class MenuRequestDto {
    private String menuName;
    private String menuContents;
    private Integer menuPrice;
    private MenuStatus menuStatus;
    private MultipartFile image;
}
