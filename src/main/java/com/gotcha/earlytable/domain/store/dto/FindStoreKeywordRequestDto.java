package com.gotcha.earlytable.domain.store.dto;

import lombok.Getter;

@Getter
public class FindStoreKeywordRequestDto {

    private final String keyword;

    public FindStoreKeywordRequestDto(String keyword) {
        this.keyword = keyword;
    }
}
