package com.gotcha.earlytable.domain.allergy.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AllergyStuffRequestDto {
    @NotBlank(message = "알러지 원재료명을 입력하세요.")
    private String allergyStuff;

    @JsonCreator
    public AllergyStuffRequestDto(String allergyStuff) {
        this.allergyStuff = allergyStuff;
    }
}
