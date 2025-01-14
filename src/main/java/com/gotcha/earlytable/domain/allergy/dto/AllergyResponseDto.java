package com.gotcha.earlytable.domain.allergy.dto;

import com.gotcha.earlytable.domain.allergy.AllergyCategoryRepository;
import com.gotcha.earlytable.domain.allergy.entity.Allergy;
import lombok.Getter;

@Getter
public class AllergyResponseDto {
    private Long allergyId;
    private String allergyCategory;
    private String allergyStuff;

    public AllergyResponseDto(Long allergyId, String allergyCategory, String allergyStuff) {
        this.allergyId = allergyId;
        this.allergyCategory = allergyCategory;
        this.allergyStuff = allergyStuff;
    }
}
