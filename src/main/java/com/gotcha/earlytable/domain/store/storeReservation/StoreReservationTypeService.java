package com.gotcha.earlytable.domain.store.storeReservation;

import com.gotcha.earlytable.domain.store.StoreRepository;
import com.gotcha.earlytable.domain.store.dto.StoreReservationTypeRequestDto;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.entity.StoreReservationType;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.Auth;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.ConflictException;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import com.gotcha.earlytable.global.error.exception.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
     * @param user
     * @param requestDto
     */
    @Transactional
    public void createStoreReservationType(Long storeId, User user, StoreReservationTypeRequestDto requestDto) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 본인 가게인지 확인
        if(user.getAuth().equals(Auth.OWNER) && !store.getUser().getId().equals(user.getId())) {
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
     * @param user
     * @param requestDto
     */
    @Transactional
    public void deleteStoreReservationType(Long storeId, User user, StoreReservationTypeRequestDto requestDto) {

        Optional<StoreReservationType> storeReservationType = storeReservationTypeRepository.findByStoreStoreId(storeId);

        // 존재하지 않으면 예외처리
        if(storeReservationType.isEmpty()) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }

        // 본인 가게인지 확인
        if(user.getAuth().equals(Auth.OWNER) && !storeReservationType.get().getStore().getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }

        // 삭제
        storeReservationTypeRepository
                .deleteByStoreReservationTypeIdAndReservationType(storeReservationType.get().getStoreReservationTypeId(),
                        requestDto.getReservationType());
    }
}
