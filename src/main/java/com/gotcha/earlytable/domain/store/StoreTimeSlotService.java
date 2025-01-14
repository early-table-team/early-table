package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.store.dto.TimeSlotResponseDto;
import com.gotcha.earlytable.domain.store.dto.TimeSlotRequestDto;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.entity.StoreTimeSlot;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.CustomException;
import com.gotcha.earlytable.global.error.exception.ForbiddenException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StoreTimeSlotService implements ValidateStore {

    private final StoreTimeSlotRepository storeTimeSlotRepository;
    private final StoreRepository storeRepository;


    public StoreTimeSlotService(StoreTimeSlotRepository storeTimeSlotRepository, StoreRepository storeRepository) {
        this.storeTimeSlotRepository = storeTimeSlotRepository;
        this.storeRepository = storeRepository;
    }

    /**
     * 타임슬롯 생성 메서드
     *
     * @param storeId
     * @param requestDto
     * @param user
     * @return TimeSlotResponseDto
     */
    @Transactional
    public TimeSlotResponseDto createStoreTimeSlot(Long storeId, TimeSlotRequestDto requestDto, User user) {

        // 본인 가게 확인
        validateStoreOwner(storeId, user.getId());

        // 가게와 시간대를 이용하여 이미 존재하는 값인지 구분
        boolean isExist = storeTimeSlotRepository
                .existsByStoreStoreIdAndReservationTime(storeId, requestDto.getReservationTime());
        if (isExist) {
            throw new CustomException(ErrorCode.DUPLICATE_VALUE);
        }

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        StoreTimeSlot storeTimeSlot = new StoreTimeSlot(requestDto.getReservationTime(), store);
        storeTimeSlotRepository.save(storeTimeSlot);

        return new TimeSlotResponseDto(storeTimeSlot.getStoreTimeSlotId(), storeTimeSlot.getReservationTime());
    }


    /**
     * 타임슬롯 전체조회 메서드
     *
     * @param storeId
     * @return List<TimeSlotResponseDto>
     */
    public List<TimeSlotResponseDto> getAllTimeSlots(Long storeId) {

        List<StoreTimeSlot> timeSlots = storeTimeSlotRepository.findByStoreStoreId(storeId);

        return timeSlots.stream()
                .map(storeTimeSlot -> new TimeSlotResponseDto(storeTimeSlot.getStoreTimeSlotId(), storeTimeSlot.getReservationTime()))
                .toList();

    }

    /**
     * 타임슬롯 단일 조회
     *
     * @param timeSlotId
     * @return TimeSlotResponseDto
     */
    public TimeSlotResponseDto getOneTimeSlot(Long timeSlotId, Long storeId) {

        StoreTimeSlot storeTimeSlot = storeTimeSlotRepository.findByIdOrElseThrow(timeSlotId);

        // 해당 가게의 타임슬롯인지 확인
        if(!storeTimeSlot.getStore().getStoreId().equals(storeId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_ACCESS_PTAH);
        }

        return new TimeSlotResponseDto(storeTimeSlot.getStoreTimeSlotId(), storeTimeSlot.getReservationTime());
    }

    /**
     * 타임슬롯 수정 메서드
     *
     * @param storeId
     * @param timeSlotId
     * @param requestDto
     * @param user
     * @return TimeSlotResponseDto
     */
    @Transactional
    public TimeSlotResponseDto modifyTimeSlot(Long storeId, Long timeSlotId, TimeSlotRequestDto requestDto, User user) {

        // 본인가게 확인
        validateStoreOwner(storeId, user.getId());

        StoreTimeSlot storeTimeSlot = storeTimeSlotRepository.findByIdOrElseThrow(timeSlotId);

        // 해당 가게의 타임슬롯인지 확인
        if(!storeTimeSlot.getStore().getStoreId().equals(storeId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_ACCESS_PTAH);
        }

        // 가게와 시간대를 이용하여 이미 존재하는 값인지 구분
        boolean isExist = storeTimeSlotRepository
                .existsByStoreStoreIdAndReservationTime(storeId, requestDto.getReservationTime());
        if (isExist) {
            throw new CustomException(ErrorCode.DUPLICATE_VALUE);
        }

        // 정보 수정
        storeTimeSlot.changeTimeSlot(requestDto.getReservationTime());

        storeTimeSlotRepository.save(storeTimeSlot);

        return new TimeSlotResponseDto(storeTimeSlot.getStoreTimeSlotId(), storeTimeSlot.getReservationTime());
    }

    /**
     * 타임슬롯 삭제 메서드
     *
     * @param storeId
     * @param timeSlotId
     * @param user
     */
    @Transactional
    public void deleteTimeSlot(Long storeId, Long timeSlotId, User user) {

        // 본인 가게 확인
        validateStoreOwner(storeId, user.getId());

        // 해당 가게의 타임슬롯인지 확인
        StoreTimeSlot storeTimeSlot = storeTimeSlotRepository.findByIdOrElseThrow(timeSlotId);

        // 해당 가게의 타임슬롯인지 확인
        if(!storeTimeSlot.getStore().getStoreId().equals(storeId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_ACCESS_PTAH);
        }

        // 타임슬롯 삭제
        storeTimeSlotRepository.deleteById(timeSlotId);
    }


    /**
     * 본인 가게인지 확인하는 메서드
     *
     * @param storeId
     * @param userId
     */
    @Override
    public void validateStoreOwner(Long storeId, Long userId) {

        // 본인 가게 인지 확인
        boolean isOwner = storeRepository.existsByStoreIdAndUserId(storeId, userId);
        if (!isOwner) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }
}
