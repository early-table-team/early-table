package com.gotcha.earlytable.domain.keyword.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class StoreKeywordRequestDto {

    @NotNull
    private final Long keywordId;

    @NotNull
    private final List<Long> storeIds;

    public StoreKeywordRequestDto(Long keywordId, List<Long> storeIds) {
        this.keywordId = keywordId;
        this.storeIds = storeIds;
    }
}
