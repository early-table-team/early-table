package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.store.dto.StoreListResponseDto;
import com.gotcha.earlytable.domain.store.dto.StoreSearchRequestDto;

import java.util.List;

public interface StoreRepositoryQuery {

    List<StoreListResponseDto> searchStoreQuery(StoreSearchRequestDto requestDto);
}
