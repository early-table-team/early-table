package com.gotcha.earlytable.domain.allergy.dto;

import com.gotcha.earlytable.domain.allergy.entity.AllergyStuff;
import lombok.Getter;

@Getter
public class AllergyStuffResponseDto {
    private Long allergyStuffId;
    private String allergyStuff;

    public AllergyStuffResponseDto(Long allergyStuffId, String allergyStuff) {
        this.allergyStuffId = allergyStuffId;
        this.allergyStuff = allergyStuff;
    }

    public static AllergyStuffResponseDto toDto(AllergyStuff allergyStuff) {
        return new AllergyStuffResponseDto(
                allergyStuff.getAllergyStuffId(),
                allergyStuff.getAllergyStuff()
        );
    }
}
