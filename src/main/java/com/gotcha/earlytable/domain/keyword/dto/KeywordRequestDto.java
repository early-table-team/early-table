package com.gotcha.earlytable.domain.keyword.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class KeywordRequestDto {

    @NotBlank
    @Size(min = 1, max = 20, message = "키워드는 10자 부터 20자 까지입니다.")
    private final String keyword;

    public KeywordRequestDto(String keyword) {
        this.keyword = keyword;
    }
}
