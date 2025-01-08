package com.gotcha.earlytable.domain.menu.dto;

import com.gotcha.earlytable.domain.menu.entity.AllergyCategory;
import com.gotcha.earlytable.domain.menu.entity.AllergyStuff;
import lombok.Getter;

@Getter
public class AllergyCategoryResponseDto {
    private String allergyCategory;

    public AllergyCategoryResponseDto(String allergyCategory) {
        this.allergyCategory = allergyCategory;
    }

    public static AllergyCategoryResponseDto toDto(AllergyCategory allergyCategory) {
        return new AllergyCategoryResponseDto(
                allergyCategory.getAllergyCategory()
        );
    }
}
