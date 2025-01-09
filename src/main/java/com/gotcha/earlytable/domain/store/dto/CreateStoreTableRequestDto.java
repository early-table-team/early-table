package com.gotcha.earlytable.domain.store.dto;

import lombok.Getter;

@Getter
public class CreateStoreTableRequestDto {

    private final Integer tableMaxNumber;

    private final Integer tableCount;

    public CreateStoreTableRequestDto(Integer tableMaxNumber, Integer tableCount) {
        this.tableMaxNumber = tableMaxNumber;
        this.tableCount = tableCount;
    }
}
