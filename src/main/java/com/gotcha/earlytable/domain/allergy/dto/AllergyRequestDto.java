package com.gotcha.earlytable.domain.allergy.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AllergyRequestDto {
    @NotBlank(message = "알러지 유발 원재료를 입력하세요.")
    private String allergyStuff;

    @JsonCreator
    public AllergyRequestDto(String allergyStuff) {
        this.allergyStuff = allergyStuff;
    }
}
