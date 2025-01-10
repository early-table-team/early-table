package com.gotcha.earlytable.domain.allergy.dto;

import com.gotcha.earlytable.domain.allergy.entity.AllergyCategory;
import lombok.Getter;

@Getter
public class AllergyCategoryResponseDto {
    private Long allergyCategoryId;
    private String allergyCategory;

    public AllergyCategoryResponseDto(Long allergyCategoryId, String allergyCategory) {
        this.allergyCategoryId = allergyCategoryId;
        this.allergyCategory = allergyCategory;
    }

    public static AllergyCategoryResponseDto toDto(AllergyCategory allergyCategory) {
        return new AllergyCategoryResponseDto(
                allergyCategory.getAllergyCategoryId(),
                allergyCategory.getAllergyCategory()
        );
    }
}
