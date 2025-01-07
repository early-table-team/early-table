package com.gotcha.earlytable.domain.store.enums;

import lombok.Getter;

@Getter
public enum StoreCategory {
    KOREAN("한식"),
    WESTERN("양식"),
    JAPANESE("일식"),
    CHINESE("중식"),
    ASIAN("아시안"),
    DESSERT("디저트");

    private final String categoryName;

    StoreCategory(String categoryName) {
        this.categoryName = categoryName;
    }

}
