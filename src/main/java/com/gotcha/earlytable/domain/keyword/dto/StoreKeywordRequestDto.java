package com.gotcha.earlytable.domain.keyword.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class StoreKeywordRequestDto {

    private final Long keywordId;

    private final List<Long> storeIds;

    public StoreKeywordRequestDto(Long keywordId, List<Long> storeIds) {
        this.keywordId = keywordId;
        this.storeIds = storeIds;
    }
}
