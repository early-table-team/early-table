package com.gotcha.earlytable.domain.store.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class StoreRestRequestDto {

    private final LocalDate storeOffDay;

    private final String storeOffReason;

    public StoreRestRequestDto(LocalDate storeOffDay, String storeOffReason) {
        this.storeOffDay = storeOffDay;
        this.storeOffReason = storeOffReason;
    }
}
