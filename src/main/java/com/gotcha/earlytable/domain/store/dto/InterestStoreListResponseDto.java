package com.gotcha.earlytable.domain.store.dto;


import lombok.Getter;
import java.util.List;

@Getter
public class InterestStoreListResponseDto {

    private List<InterestStoreResponseDto> stores;

    public InterestStoreListResponseDto(List<InterestStoreResponseDto> stores) {
        this.stores = stores;
    }

}
