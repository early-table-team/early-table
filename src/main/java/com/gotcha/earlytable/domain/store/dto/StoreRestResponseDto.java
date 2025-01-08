package com.gotcha.earlytable.domain.store.dto;

import com.gotcha.earlytable.domain.store.entity.StoreRest;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class StoreRestResponseDto {

    private final Long storeId;

    private final Long storeRestId;

    private final LocalDate storeOffDay;

    private final String storeOffReason;

    public StoreRestResponseDto(Long storeId, Long storeRestId, LocalDate storeOffDay, String storeOffReason) {
        this.storeId = storeId;
        this.storeRestId = storeRestId;
        this.storeOffDay = storeOffDay;
        this.storeOffReason = storeOffReason;
    }

    public static StoreRestResponseDto toDto(StoreRest storeRest) {
        return new StoreRestResponseDto(
                storeRest.getStore().getStoreId(),
                storeRest.getStoreRestId(),
                storeRest.getStoreOffDay(),
                storeRest.getStoreOffReason()
        );
    }
}
