package com.gotcha.earlytable.domain.keyword.dto;

import lombok.Getter;

@Getter
public class KeywordRequestDto {

    private final String keyword;

    public KeywordRequestDto(String keyword) {
        this.keyword = keyword;
    }
}
