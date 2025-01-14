package com.gotcha.earlytable.domain.store.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class StoreTableCreateRequestDto {

    @NotNull
    @Min(0)
    @Max(200)
    private final Integer tableMaxNumber;

    @NotNull
    @Min(0)
    @Max(1000)
    private final Integer tableCount;

    public StoreTableCreateRequestDto(Integer tableMaxNumber, Integer tableCount) {
        this.tableMaxNumber = tableMaxNumber;
        this.tableCount = tableCount;
    }
}
