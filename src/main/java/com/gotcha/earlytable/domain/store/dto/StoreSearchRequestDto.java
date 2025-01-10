package com.gotcha.earlytable.domain.store.dto;

import com.gotcha.earlytable.domain.store.enums.StoreCategory;
import lombok.Getter;

import java.util.List;

@Getter
public class StoreSearchRequestDto {
    private final String searchWord;
    private final String regionTop;
    private final String regionBottom;
    private final Long minPrice;
    private final Long maxPrice;
    private final StoreCategory storeCategory;
    private final List<String> allergyCategory;
    private final List<String> allergyStuff;


    public StoreSearchRequestDto(String searchWord, String regionTop, String regionBottom, Long minPrice, Long maxPrice, StoreCategory storeCategory, List<String> allergyCategory, List<String> allergyStuff) {
        this.searchWord = searchWord;
        this.regionTop = regionTop;
        this.regionBottom = regionBottom;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.storeCategory = storeCategory;
        this.allergyCategory = allergyCategory;
        this.allergyStuff = allergyStuff;
    }
}
