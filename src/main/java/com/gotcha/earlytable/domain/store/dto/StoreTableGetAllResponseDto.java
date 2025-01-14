package com.gotcha.earlytable.domain.store.dto;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;

@Getter
public class StoreTableGetAllResponseDto {

    private final Long storeId;

    private final List<HashMap<String, Long>> storeTable;

    public StoreTableGetAllResponseDto(Long storeId, List<HashMap<String, Long>> storeTable) {
        this.storeId = storeId;
        this.storeTable = storeTable;
    }

}
