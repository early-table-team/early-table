package com.gotcha.earlytable.domain.menu.dto;

import com.gotcha.earlytable.domain.menu.entity.AllergyDetail;
import lombok.Getter;

@Getter
public class AllergyDetailResponseDto {
    private String allergyName;
    private String allergyStuff;
    private String allergyContents;

    public AllergyDetailResponseDto(String allergyName, String allergyStuff, String allergyContents) {
        this.allergyName = allergyName;
        this.allergyStuff = allergyStuff;
        this.allergyContents = allergyContents;
    }

    public static AllergyDetailResponseDto toDto(AllergyDetail allergyDetail) {
        return new AllergyDetailResponseDto(
                allergyDetail.getAllergyName(),
                allergyDetail.getAllergyStuff(),
                allergyDetail.getAllergyContents()
        );
    }
}
