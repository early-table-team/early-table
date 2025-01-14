package com.gotcha.earlytable.domain.store.dto;

import com.gotcha.earlytable.domain.store.enums.StoreStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class StoreStatusRequestDto {

    @NotNull
    private final StoreStatus storeStatus;

    public StoreStatusRequestDto(StoreStatus storeStatus) {
        this.storeStatus = storeStatus;
    }

}
