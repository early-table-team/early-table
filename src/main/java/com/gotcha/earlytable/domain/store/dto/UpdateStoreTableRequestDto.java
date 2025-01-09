package com.gotcha.earlytable.domain.store.dto;

import lombok.Getter;

@Getter
public class UpdateStoreTableRequestDto {

    private final Integer tableMaxNumber;

    private final Integer tableCount;

    public UpdateStoreTableRequestDto(final Integer tableMaxNumber, final Integer tableCount) {
        this.tableMaxNumber = tableMaxNumber;
        this.tableCount = tableCount;
    }
}
