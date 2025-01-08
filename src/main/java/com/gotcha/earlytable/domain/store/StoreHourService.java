package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.store.dto.StoreHourRequestDto;
import com.gotcha.earlytable.domain.store.dto.StoreHourResponseDto;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.entity.StoreHour;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.ConflictException;
import com.gotcha.earlytable.global.error.exception.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class StoreHourService {

    private final StoreHourRepository storeHourRepository;
    private final StoreRepository storeRepository;

    public StoreHourService(StoreHourRepository storeHourRepository, StoreRepository storeRepository) {
        this.storeHourRepository = storeHourRepository;
        this.storeRepository = storeRepository;
    }

    /**
     * 가게 영업시간 등록 메서드
     *
     * @param storeId
     * @param userId
     * @param requestDto
     * @return StoreHourResponseDto
     */
    @Transactional
    public StoreHourResponseDto createStoreHour(Long storeId, Long userId, StoreHourRequestDto requestDto) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 자신의 가게인지 확인
        if(store.getUser().getId().equals(userId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }

        boolean isStoreHour = store.getStoreHourList().stream()
                .anyMatch(storeHour -> storeHour.getDayOfWeek().equals(requestDto.getDayOfWeek()));

        // 중복 확인
        if (isStoreHour) {
            throw new ConflictException(ErrorCode.DUPLICATE_VALUE);
        }

        // 영업시간 객체 생성
        StoreHour storeHour = new StoreHour(requestDto.getDayOfWeek(), requestDto.getOpenTime(),
                                            requestDto.getClosedTime(), requestDto.getDayStatus(), store);

        // 저장
        StoreHour savedStoreHour = storeHourRepository.save(storeHour);

        return StoreHourResponseDto.toDto(savedStoreHour);
    }

    /**
     * 가게 영업시간 수정 메서드
     *
     * @param storeHourId
     * @param userId
     * @param requestDto
     * @return StoreHourResponseDto
     */
    public StoreHourResponseDto updateStoreHour(Long storeHourId, Long userId, StoreHourRequestDto requestDto) {

        StoreHour storeHour = storeHourRepository.findByIdOrElseThrow(storeHourId);

        // 자신의 가게인지 확인
        if(storeHour.getStore().getUser().getId().equals(userId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }

        // 수정
        storeHour.updateStoreHour(requestDto);

        // 저장
        StoreHour savedStoreHour = storeHourRepository.save(storeHour);

        return StoreHourResponseDto.toDto(savedStoreHour);
    }
}
