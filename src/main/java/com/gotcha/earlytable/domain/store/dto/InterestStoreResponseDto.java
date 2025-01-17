package com.gotcha.earlytable.domain.store.dto;

import com.gotcha.earlytable.domain.store.enums.StoreCategory;
import lombok.Getter;

@Getter
public class InterestStoreResponseDto {

    private final Long storeId;

    private final String storeName;

    private final String storeContents;

    private final StoreCategory storeCategory;

    private final String presentMenu;

    private final Double averageRating;

    private final Long countReview;

    private final String storeImageUrl;

    public InterestStoreResponseDto(Long storeId, String storeName, String storeContents, StoreCategory storeCategory,
                                    String presentMenu, Double averageRating, Long countReview, String storeImageUrl) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeContents = storeContents;
        this.storeCategory = storeCategory;
        this.presentMenu = presentMenu;
        this.averageRating = averageRating;
        this.countReview = countReview;
        this.storeImageUrl = storeImageUrl;
    }
}
