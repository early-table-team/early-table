package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.store.dto.CreateStoreTableRequestDto;
import com.gotcha.earlytable.domain.store.dto.StoreTableGetAllResponseDto;
import com.gotcha.earlytable.domain.store.dto.UpdateStoreTableRequestDto;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.entity.StoreTable;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     *  스토어 테이블 정보변경 메서드
     * @param storeId
     * @param storeTableId
     * @param requestDto
     */
    @Transactional
    public void updateStoreTable(Long storeId, Long storeTableId, UpdateStoreTableRequestDto requestDto, User user){

        boolean isOwner = storeRepository.existsByStoreIdAndUserId(storeId, user.getId());
        if (!isOwner) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        Store store =  storeRepository.findByIdOrElseThrow(storeId);
        // 바꾸려고 하는 스토어 테이블 정보를 가져옴
        StoreTable storeTable = storeTableRepository.findById(storeTableId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        // 바꾸고자 하는 데이터로 스토어테이블 정보를 변경
        storeTable.changeTableMaxNumber(requestDto.getTableMaxNumber());
        storeTable.changeTableCount(requestDto.getTableCount());

        // 바뀐 자리수에 해당하는 정보가 이미 있는지 검사
        boolean exist = storeTableRepository.existsByStoreAndTableMaxNumber(store, storeTable.getTableMaxNumber());
        if(exist){
            throw new CustomException(ErrorCode.DUPLICATE_VALUE);
        }
        storeTableRepository.save(storeTable);
    }

    /**
     *  가게 내 모든 테이블 조회
     * @param storeId
     * @return  StoreTableGetAllResponseDto
     */
    public StoreTableGetAllResponseDto getAllStoreTable(Long storeId, User user){

        boolean isOwner = storeRepository.existsByStoreIdAndUserId(storeId, user.getId());
        if (!isOwner) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        Store store =  storeRepository.findByIdOrElseThrow(storeId);
        List<HashMap<String, Integer>> storeTableList = storeTableRepository.findAllByStore(store).stream().map( st -> {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("tableMaxNumber", st.getTableMaxNumber());
            map.put("tableCount", st.getTableCount());
            return map;
        }).toList();

        return new StoreTableGetAllResponseDto(storeId, storeTableList);

    }

    /**
     *  가게자리정보 삭제 메서드
     * @param storeId
     * @param storeTableId
     * @param user
     */
    @Transactional
    public void deleteStoreTable(Long storeId, Long storeTableId, User user) {
        //가게 주인인지 검증
        boolean isOwner = storeRepository.existsByStoreIdAndUserId(storeId, user.getId());
        if (!isOwner) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        storeTableRepository.deleteById(storeTableId);

    }
}
