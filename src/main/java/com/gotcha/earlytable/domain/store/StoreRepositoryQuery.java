package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.store.dto.StoreListResponseDto;
import com.gotcha.earlytable.domain.store.dto.StoreSearchRequestDto;
import com.gotcha.earlytable.domain.store.dto.StoreSearchResponseDto;

import java.util.List;

public interface StoreRepositoryQuery {

    List<StoreSearchResponseDto> searchStoreQuery(StoreSearchRequestDto requestDto);
}
