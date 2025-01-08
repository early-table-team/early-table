package com.gotcha.earlytable.domain.store.dto;

import com.gotcha.earlytable.domain.store.enums.StoreStatus;
import lombok.Getter;

@Getter
public class StoreStatusRequestDto {

    private final StoreStatus storeStatus;

    public StoreStatusRequestDto(StoreStatus storeStatus) {
        this.storeStatus = storeStatus;
    }

}
