package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.store.dto.TimeSlotResponseDto;
import com.gotcha.earlytable.domain.store.dto.TimeSlotRequestDto;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.entity.StoreTimeSlot;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.BadRequestException;
import com.gotcha.earlytable.global.error.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoreTimeSlotService {

    private final StoreTimeSlotRepository storeTimeSlotRepository ;
    private final StoreRepository storeRepository;


    public StoreTimeSlotService(StoreTimeSlotRepository storeTimeSlotRepository, StoreRepository storeRepository) {
        this.storeTimeSlotRepository = storeTimeSlotRepository ;
        this.storeRepository = storeRepository;
    }

    /**
     *  타임슬롯 생성 메서드
     * @param storeId
     * @param requestDto
     * @param user
     * @return TimeSlotResponseDto
     */
    @Transactional
    public TimeSlotResponseDto createStoreTimeSlot(Long storeId, TimeSlotRequestDto requestDto, User user) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);
        //본인 가게가 아닌경우
        if(store.getUser() != user){
            throw new CustomException(ErrorCode.NO_STORE_OWNER);
        }

        // 가게자리와 시간대를 이용하여 이미 존재하는 값인지 구분
        boolean exist = storeTimeSlotRepository.existsByStoreAndReservationTime(store, requestDto.getReservationTime());
        if(exist){
            throw new CustomException(ErrorCode.DUPLICATE_VALUE);
        }

        StoreTimeSlot storeTimeSlot = new StoreTimeSlot(requestDto.getReservationTime(),store );
        storeTimeSlotRepository.save(storeTimeSlot);

        return new TimeSlotResponseDto(storeTimeSlot.getStoreTimeSlotId(), storeTimeSlot.getReservationTime());
    }


    /**
     *  타임슬롯 전체조회 메서드
     * @param storeId
     * @param user
     * @return List<TimeSlotResponseDto>
     */
    public List<TimeSlotResponseDto> getAllTimeSlots(Long storeId, User user) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);
        //본인 가게가 아닌경우
        if(store.getUser() != user){
            throw new BadRequestException(ErrorCode.NO_STORE_OWNER);
        }
        List<StoreTimeSlot> timeSlots = storeTimeSlotRepository.findByStore(store);

        return timeSlots.stream()
                .map(storeTimeSlot -> new TimeSlotResponseDto(storeTimeSlot.getStoreTimeSlotId(), storeTimeSlot.getReservationTime()))
                .toList();

    }

    /**
     *  타임슬롯 단일 조회
     * @param storeId
     * @param timeSlotId
     * @param user
     * @return  TimeSlotResponseDto
     */
    public TimeSlotResponseDto getOneTimeSlot(Long storeId, Long timeSlotId, User user) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);
        //본인 가게가 아닌경우
        if(store.getUser() != user){
            throw new BadRequestException(ErrorCode.NO_STORE_OWNER);
        }

        Optional<StoreTimeSlot> storeTimeSlot = storeTimeSlotRepository.findById(timeSlotId);

        if (storeTimeSlot.isPresent()) {
            return new TimeSlotResponseDto(storeTimeSlot.get().getStoreTimeSlotId(), storeTimeSlot.get().getReservationTime());
        } else {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
    }

    /**
     *  타임슬롯 수정 메서드
     * @param storeId
     * @param timeSlotId
     * @param requestDto
     * @param user
     * @return TimeSlotResponseDto
     */
    public TimeSlotResponseDto modifyTimeSlot(Long storeId, Long timeSlotId, TimeSlotRequestDto requestDto, User user) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);
        //본인 가게가 아닌경우
        if(store.getUser() != user){
            throw new BadRequestException(ErrorCode.NO_STORE_OWNER);
        }
        Optional<StoreTimeSlot> opStoreTimeSlot = storeTimeSlotRepository.findById(timeSlotId);

        if (opStoreTimeSlot.isPresent()) {
            StoreTimeSlot storeTimeSlot = opStoreTimeSlot.get();
            storeTimeSlot.changeTimeSlot(requestDto.getReservationTime());
            storeTimeSlotRepository.save(storeTimeSlot);
            return new TimeSlotResponseDto(storeTimeSlot.getStoreTimeSlotId(), storeTimeSlot.getReservationTime());
        } else {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
    }

    /**
     *  타임슬롯 삭제 메서드
     * @param storeId
     * @param timeSlotId
     * @param user
     */
    public void deleteTimeSlot(Long storeId, Long timeSlotId, User user) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);
        //본인 가게가 아닌경우
        if(store.getUser() != user){
            throw new BadRequestException(ErrorCode.NO_STORE_OWNER);
        }

        Optional<StoreTimeSlot> storeTimeSlot = storeTimeSlotRepository.findById(timeSlotId);

        if (storeTimeSlot.isPresent()) {
            storeTimeSlotRepository.deleteById(timeSlotId);
        } else {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

    }

}
