package com.gotcha.earlytable.domain.allergy.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AllergyCategoryRequestDto {
    @NotBlank(message = "알러지 카테고리명을 입력하세요.")
    private String allergyCategory;

    @JsonCreator
    public AllergyCategoryRequestDto(String allergyCategory) {
        this.allergyCategory = allergyCategory;
    }
}
