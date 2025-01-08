package com.gotcha.earlytable.domain.keyword.dto;

import com.gotcha.earlytable.domain.keyword.entity.Keyword;
import lombok.Getter;

@Getter
public class KeywordResponseDto {

    private final Long keywordId;

    private final String keyword;


    public KeywordResponseDto(Long keywordId, String keyword) {
        this.keywordId = keywordId;
        this.keyword = keyword;
    }

    public static KeywordResponseDto toDto(Keyword keyword) {
        return new KeywordResponseDto(keyword.getKeywordId(), keyword.getKeyword());
    }
}
