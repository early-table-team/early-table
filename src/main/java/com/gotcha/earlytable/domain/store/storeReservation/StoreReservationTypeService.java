package com.gotcha.earlytable.domain.store.storeReservation;

import com.gotcha.earlytable.domain.store.StoreRepository;
import com.gotcha.earlytable.domain.store.dto.StoreReservationTypeRequestDto;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.entity.StoreReservationType;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.ConflictException;
import com.gotcha.earlytable.global.error.exception.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoreReservationTypeService {

    private final StoreReservationTypeRepository storeReservationTypeRepository;
    private final StoreRepository storeRepository;

    public StoreReservationTypeService(StoreReservationTypeRepository storeReservationTypeRepository,
                                       StoreRepository storeRepository) {

        this.storeReservationTypeRepository = storeReservationTypeRepository;
        this.storeRepository = storeRepository;
    }

    /**
     * 가게 예약 타입 설정 메서드
     *
     * @param storeId
     * @param userId
     * @param requestDto
     */
    @Transactional
    public void createStoreReservationType(Long storeId, Long userId, StoreReservationTypeRequestDto requestDto) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 본인 가게인지 확인
        if(!store.getUser().getId().equals(userId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }

        // 이미 해당 타입이 존재하는지 확인
        boolean isExist = storeReservationTypeRepository
                .existsByStoreStoreIdAndReservationType(storeId, requestDto.getReservationType());

        if (isExist) {
            throw new ConflictException(ErrorCode.DUPLICATE_VALUE);
        }

        // 예약 타입 설정
        StoreReservationType storeReservationType = new StoreReservationType(requestDto.getReservationType(), store);

        storeReservationTypeRepository.save(storeReservationType);
    }

    /**
     * 가게 예약 타입 제거 메서드
     *
     * @param storeId
     * @param userId
     * @param requestDto
     */
    public void deleteStoreReservationType(Long storeId, Long userId, StoreReservationTypeRequestDto requestDto) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 본인 가게인지 확인
        if(!store.getUser().getId().equals(userId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }

        // 삭제
        storeReservationTypeRepository.deleteByStoreStoreIdAndReservationType(storeId, requestDto.getReservationType());
    }
}
