package com.gotcha.earlytable.domain.store.dto;

import com.gotcha.earlytable.domain.store.enums.StoreCategory;
import lombok.Getter;

@Getter
public class StoreSearchRequestDto {
    private final String searchWord;
    private final String regionTop;
    private final String regionBottom;
    private final Long MinPrice;
    private final Long MaxPrice;
    private final StoreCategory storeCategory;
    private final String Allergy;


    public StoreSearchRequestDto(String searchWord, String regionTop, String regionBottom, Long minPrice, Long maxPrice, StoreCategory storeCategory, String allergy) {
        this.searchWord = searchWord;
        this.regionTop = regionTop;
        this.regionBottom = regionBottom;
        MinPrice = minPrice;
        MaxPrice = maxPrice;
        this.storeCategory = storeCategory;
        Allergy = allergy;
    }
}
