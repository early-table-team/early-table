package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.store.dto.StoreSearchRequestDto;
import com.gotcha.earlytable.domain.store.dto.StoreSearchResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreRepositoryQuery {

    List<StoreSearchResponseDto> searchStoreQuery(StoreSearchRequestDto requestDto, Pageable pageable);
}
