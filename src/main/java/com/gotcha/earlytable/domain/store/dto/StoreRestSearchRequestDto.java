package com.gotcha.earlytable.domain.store.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class StoreRestSearchRequestDto {

    private final LocalDate startDate;

    private final LocalDate endDate;

    public StoreRestSearchRequestDto(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
