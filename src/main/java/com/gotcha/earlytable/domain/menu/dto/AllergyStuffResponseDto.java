package com.gotcha.earlytable.domain.menu.dto;

import com.gotcha.earlytable.domain.menu.entity.AllergyStuff;
import lombok.Getter;

@Getter
public class AllergyStuffResponseDto {
    private Long allergyCategoryId;
    private String allergyStuff;

    public AllergyStuffResponseDto(Long allergyCategoryId, String allergyStuff) {
        this.allergyCategoryId = allergyCategoryId;
        this.allergyStuff = allergyStuff;
    }

    public static AllergyStuffResponseDto toDto(AllergyStuff allergyStuff) {
        return new AllergyStuffResponseDto(
                allergyStuff.getAllergyCategory().getAllergyCategoryId(),
                allergyStuff.getAllergyStuff()
        );
    }
}
