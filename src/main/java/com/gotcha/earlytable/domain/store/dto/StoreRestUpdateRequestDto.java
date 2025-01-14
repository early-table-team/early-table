package com.gotcha.earlytable.domain.store.dto;

import lombok.Getter;

@Getter
public class StoreRestUpdateRequestDto {

    private final String storeOffReason;

    public StoreRestUpdateRequestDto(String storeOffReason) {
        this.storeOffReason = storeOffReason;
    }
}
