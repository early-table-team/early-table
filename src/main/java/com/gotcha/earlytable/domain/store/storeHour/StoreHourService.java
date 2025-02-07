package com.gotcha.earlytable.domain.store.storeHour;

import com.gotcha.earlytable.domain.store.StoreRepository;
import com.gotcha.earlytable.domain.store.dto.StoreHourRequestDto;
import com.gotcha.earlytable.domain.store.dto.StoreHourResponseDto;
import com.gotcha.earlytable.domain.store.dto.StoreHourUpdateRequestDto;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.entity.StoreHour;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.Auth;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.ConflictException;
import com.gotcha.earlytable.global.error.exception.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


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
     * @param user
     * @param requestDto
     * @return WaitingSettingResponseDto
     */
    @Transactional
    public StoreHourResponseDto createStoreHour(Long storeId, User user, StoreHourRequestDto requestDto) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 자신의 가게인지 확인
        if (user.getAuth().equals(Auth.OWNER) && !store.getUser().getId().equals(user.getId())) {
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
     * @param user
     * @param requestDto
     * @return WaitingSettingResponseDto
     */
    @Transactional
    public StoreHourResponseDto updateStoreHour(Long storeHourId, User user, StoreHourUpdateRequestDto requestDto) {

        StoreHour storeHour = storeHourRepository.findByIdOrElseThrow(storeHourId);

        // 자신의 가게인지 확인
        if (user.getAuth().equals(Auth.OWNER) && !storeHour.getStore().getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }

        // 수정
        storeHour.updateStoreHour(requestDto);

        // 저장
        StoreHour savedStoreHour = storeHourRepository.save(storeHour);

        return StoreHourResponseDto.toDto(savedStoreHour);
    }

    public List<StoreHourResponseDto> getStoreHour(Long storeId, User user) {
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        List<StoreHour> storeHourList = storeHourRepository.findByStore(store);

        return storeHourList.stream()
                .map(StoreHourResponseDto::toDto) // StoreHour를 StoreHourResponseDto로 변환
                .collect(Collectors.toList());

    }

}
