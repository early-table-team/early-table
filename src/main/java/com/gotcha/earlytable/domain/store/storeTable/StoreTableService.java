package com.gotcha.earlytable.domain.store.storeTable;

import com.gotcha.earlytable.domain.store.StoreRepository;
import com.gotcha.earlytable.domain.store.dto.StoreTableCreateRequestDto;
import com.gotcha.earlytable.domain.store.dto.StoreTableGetAllResponseDto;
import com.gotcha.earlytable.domain.store.dto.StoreTableUpdateRequestDto;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.entity.StoreTable;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.Auth;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.CustomException;
import com.gotcha.earlytable.global.error.exception.ForbiddenException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
public class StoreTableService {

    private final StoreTableRepository storeTableRepository;
    private final StoreRepository storeRepository;

    public StoreTableService(StoreTableRepository storeTableRepository, StoreRepository storeRepository) {
        this.storeTableRepository = storeTableRepository;
        this.storeRepository = storeRepository;
    }

    /**
     * StoreTable 생성 메서드
     *
     * @param storeId
     * @param requestDto
     */
    @Transactional
    public void createStoreTable(Long storeId, StoreTableCreateRequestDto requestDto, User user) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 본인 가게 확인
        validateStoreOwner(store, user);

        // 이미 존재하는지 확인
        boolean exists = storeTableRepository.existsByStoreAndTableMaxNumber(store, requestDto.getTableMaxNumber());
        if (exists) {
            throw new CustomException(ErrorCode.DUPLICATE_VALUE);
        }

        StoreTable storeTable = new StoreTable(store, requestDto.getTableMaxNumber(), requestDto.getTableCount());
        storeTableRepository.save(storeTable);

    }

    /**
     * 스토어 테이블 정보변경 메서드
     *
     * @param storeId
     * @param storeTableId
     * @param requestDto
     */
    @Transactional
    public void updateStoreTable(Long storeId, Long storeTableId, StoreTableUpdateRequestDto requestDto, User user) {

        // 바꾸려고 하는 스토어 테이블 정보를 가져옴
        StoreTable storeTable = storeTableRepository.findByIdOrElseThrow(storeTableId);

        // 본인 가게 확인
        validateStoreOwner(storeTable.getStore(), user);

        // 해당 자리 테이블 정보가 입력받은 가게의 테이블 정보인지 확인
        if (!storeTable.getStore().getStoreId().equals(storeId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_ACCESS_PTAH);
        }

        // 바꾸고자 하는 데이터로 스토어테이블 정보를 변경
        storeTable.changeTableCount(requestDto.getTableCount());

        storeTableRepository.save(storeTable);
    }

    /**
     * 가게 내 모든 테이블 조회
     *
     * @param storeId
     * @return StoreTableGetAllResponseDto
     */
    public StoreTableGetAllResponseDto getAllStoreTable(Long storeId) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);
        List<HashMap<String, Long>> storeTableList = storeTableRepository.findAllByStore(store).stream().map(st -> {
            HashMap<String, Long> map = new HashMap<>();

            map.put("tableId", st.getStoreTableId());
            map.put("tableMaxNumber", st.getTableMaxNumber().longValue());
            map.put("tableCount", st.getTableCount().longValue());

            return map;
        }).toList();

        return new StoreTableGetAllResponseDto(storeId, storeTableList);

    }

    /**
     * 가게자리정보 삭제 메서드
     *
     * @param storeId
     * @param storeTableId
     * @param user
     */
    @Transactional
    public void deleteStoreTable(Long storeId, Long storeTableId, User user) {

        StoreTable storeTable = storeTableRepository.findByIdOrElseThrow(storeTableId);

        // 본인 가게 확인
        validateStoreOwner(storeTable.getStore(), user);

        // 해당 테이블 정보가 입력받은 가게의 정보인지 확인
        if (!storeTable.getStore().getStoreId().equals(storeId)) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        // 삭제
        storeTableRepository.delete(storeTable);
    }

    /**
     * 본인 가게인지 확인하는 메서드
     *
     * @param store
     * @param user
     */
    public void validateStoreOwner(Store store, User user) {

        // 본인 가게 인지 확인
        if (user.getAuth().equals(Auth.OWNER) && !store.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }
}
