package com.gotcha.earlytable.domain.store.dto;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;

@Getter
public class StoreTableGetAllResponseDto {

    private final Long StoreTableId;

    private final List<HashMap<String, Integer>> storeTable;

    public StoreTableGetAllResponseDto(Long StoreTableId, List<HashMap<String, Integer>> storeTable) {
        this.StoreTableId = StoreTableId;
        this.storeTable = storeTable;
    }

}
