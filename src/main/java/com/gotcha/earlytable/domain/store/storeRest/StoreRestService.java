package com.gotcha.earlytable.domain.store.storeRest;

import com.gotcha.earlytable.domain.store.StoreRepository;
import com.gotcha.earlytable.domain.store.dto.StoreRestRequestDto;
import com.gotcha.earlytable.domain.store.dto.StoreRestResponseDto;
import com.gotcha.earlytable.domain.store.dto.StoreRestSearchRequestDto;
import com.gotcha.earlytable.domain.store.dto.StoreRestUpdateRequestDto;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.entity.StoreRest;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.ConflictException;
import com.gotcha.earlytable.global.error.exception.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class StoreRestService {

    private final StoreRestRepository storeRestRepository;
    private final StoreRepository storeRepository;

    public StoreRestService(StoreRestRepository storeRestRepository, StoreRepository storeRepository) {
        this.storeRestRepository = storeRestRepository;
        this.storeRepository = storeRepository;
    }

    /**
     * 가게 휴무일 등록 메서드
     *
     * @param storeId
     * @param userId
     * @param requestDto
     * @return StoreRestResponseDto
     */
    @Transactional
    public StoreRestResponseDto createStoreRest(Long storeId, Long userId, StoreRestRequestDto requestDto) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 본인 가게인지 확인
        if(!store.getUser().getId().equals(userId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }

        boolean isExistRest = store.getStoreRestList().stream()
                .anyMatch(storeRest -> storeRest.getStoreOffDay().equals(requestDto.getStoreOffDay()));

        // 중복 여부 확인
        if(isExistRest) {
            throw new ConflictException(ErrorCode.DUPLICATE_VALUE);
        }

        // 휴무일 객체 생성
        StoreRest storeRest = new StoreRest(requestDto.getStoreOffDay(), requestDto.getStoreOffReason(), store);

        // 저장
        StoreRest savedStoreRest = storeRestRepository.save(storeRest);

        return StoreRestResponseDto.toDto(savedStoreRest);
    }

    /**
     * 가게 휴무일 단건 조회 메서드
     *
     * @param restId
     * @return StoreRestResponseDto
     */
    public StoreRestResponseDto getStoreRest(Long restId) {

        StoreRest storeRest = storeRestRepository.findByIdOrElseThrow(restId);

        return StoreRestResponseDto.toDto(storeRest);
    }

    /**
     * 가게 휴무일 조건 조회 메서드
     *
     * @param storeId
     * @param requestDto
     * @return List<StoreRestResponseDto>
     */
    public List<StoreRestResponseDto> getAllStoreRest(Long storeId, StoreRestSearchRequestDto requestDto) {

        LocalDate startDate = requestDto.getStartDate();
        LocalDate endDate = requestDto.getEndDate();

        // 시작날이 null 이면 현재 기준
        if(startDate == null) {
            startDate = LocalDate.now();
        }

        // 마지막날이 null 이면 시작날 기준 + 30일
        if(endDate == null) {
            endDate = startDate.plusDays(30);
        }

        // 일정 구간으로 휴무일 조회하기
        List<StoreRest> storeRestList = storeRestRepository
                .findAllByStoreStoreIdAndStoreOffDayBetween(storeId, startDate, endDate);

        return storeRestList.stream().map(StoreRestResponseDto::toDto).toList();
    }

    /**
     * 가게 휴무일 수정 메서드
     *
     * @param restId
     * @param userId
     * @param requestDto
     * @return StoreRestResponseDto
     */
    @Transactional
    public StoreRestResponseDto updateStoreRest(Long restId, Long userId, StoreRestUpdateRequestDto requestDto) {

        StoreRest storeRest = storeRestRepository.findByIdOrElseThrow(restId);

        // 본인 가게인지 확인
        if(!storeRest.getStore().getUser().getId().equals(userId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }

        storeRest.updateStoreRest(requestDto);

        storeRestRepository.save(storeRest);

        return StoreRestResponseDto.toDto(storeRest);
    }

    /**
     * 가게 휴무일 삭제 메서드
     *
     * @param restId
     * @param userId
     */
    @Transactional
    public void deleteStoreRest(Long restId, Long userId) {

        StoreRest storeRest = storeRestRepository.findByIdOrElseThrow(restId);

        // 본인 가게인지 확인
        if(!storeRest.getStore().getUser().getId().equals(userId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }

        // 삭제
        storeRestRepository.deleteById(restId);
    }
}
