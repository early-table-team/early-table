package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.store.dto.CreateStoreTableRequestDto;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.entity.StoreTable;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoreTableService {

    private final StoreTableRepository storeTableRepository;
    private final StoreRepository storeRepository;

    public StoreTableService(StoreTableRepository storeTableRepository, StoreRepository storeRepository) {
        this.storeTableRepository = storeTableRepository;
        this.storeRepository = storeRepository;
    }

    /**
     *  StoreTable 생성 메서드
     * @param storeId
     * @param requestDto
     */
    @Transactional
    public void createStoreTable(Long storeId, CreateStoreTableRequestDto requestDto){

        Store store =  storeRepository.findByIdOrElseThrow(storeId);

        boolean exists = storeTableRepository.existsByStoreAndTableMaxNumber(store, requestDto.getTableMaxNumber());
        if(exists){
            throw new CustomException(ErrorCode.DUPLICATE_VALUE);
        }
        StoreTable storeTable = new StoreTable(store,requestDto.getTableMaxNumber(), requestDto.getTableCount());
        storeTableRepository.save(storeTable);

    }

}
