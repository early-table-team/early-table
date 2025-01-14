package com.gotcha.earlytable.domain.store.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class StoreTableUpdateRequestDto {

    @NotNull
    @Min(0)
    @Max(1000)
    private final Integer tableCount;

    public StoreTableUpdateRequestDto(Integer tableCount) {
        this.tableCount = tableCount;
    }
}
