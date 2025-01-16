package com.gotcha.earlytable.domain.waitingsetting;

import com.gotcha.earlytable.domain.store.StoreRepository;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.waitingsetting.dto.WaitingSettingCreateRequestDto;
import com.gotcha.earlytable.domain.waitingsetting.dto.WaitingSettingResponseDto;
import com.gotcha.earlytable.domain.waitingsetting.dto.WaitingSettingUpdateRequestDto;
import com.gotcha.earlytable.domain.waitingsetting.dto.WaitingSettingUpdateStatusResponseDto;
import com.gotcha.earlytable.domain.waitingsetting.entity.WaitingSetting;
import com.gotcha.earlytable.domain.waitingsetting.enums.WaitingSettingStatus;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.ConflictException;
import com.gotcha.earlytable.global.error.exception.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WaitingSettingService {

    private final StoreRepository storeRepository;
    private final WaitingSettingRepository waitingSettingRepository;

    public WaitingSettingService(StoreRepository storeRepository, WaitingSettingRepository waitingSettingRepository) {
        this.storeRepository = storeRepository;
        this.waitingSettingRepository = waitingSettingRepository;
    }

    /**
     * 웨이팅 설정 등록 메서드
     *
     * @param storeId
     * @param userId
     * @param requestDto
     * @return WaitingSettingResponseDto
     */
    @Transactional
    public WaitingSettingResponseDto createWaitingSetting(Long storeId, Long userId, WaitingSettingCreateRequestDto requestDto) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 자신의 가게인지 확인
        if (!store.getUser().getId().equals(userId)) {
            throw new UnauthorizedException(ErrorCode.NO_STORE_OWNER);
        }
        // 이미 존재하면 예외처리
        if(waitingSettingRepository.existsByStore(store)) {
            throw new ConflictException(ErrorCode.DUPLICATE_VALUE);
        }

        // 웨이팅 설정 생성
        WaitingSetting waitingSetting = new WaitingSetting(requestDto.getWaitingOpenTime(),
                requestDto.getWaitingClosedTime(),
                requestDto.getWaitingSettingStatus(), store);


        // 저장
        WaitingSetting savedWaitingSetting = waitingSettingRepository.save(waitingSetting);

        return WaitingSettingResponseDto.toDto(savedWaitingSetting);
    }


    /**
     * 웨이팅 설정 변경 메서드
     *
     * @param waitingSettingId
     * @param userId
     * @param requestDto
     * @return WaitingSettingResponseDto
     */
    public WaitingSettingResponseDto updateWaitingSetting(Long waitingSettingId, Long userId,
                                                          WaitingSettingUpdateRequestDto requestDto) {

        WaitingSetting waitingSetting = waitingSettingRepository.findByIdOrElseThrow(waitingSettingId);

        // 자신의 가게인지 확인
        if (!waitingSetting.getStore().getUser().getId().equals(userId)) {
            throw new UnauthorizedException(ErrorCode.NO_STORE_OWNER);
        }

        // 내용 수정
        waitingSetting.updateWaitingSetting(requestDto);

        // 저장
        WaitingSetting savedWaitingSetting = waitingSettingRepository.save(waitingSetting);

        return WaitingSettingResponseDto.toDto(savedWaitingSetting);
    }


    /**
     * 웨이팅 설정 조회 메서드
     *
     * @param waitingSettingId
     * @return WaitingSettingResponseDto
     */
    public WaitingSettingResponseDto getWaitingSetting(Long waitingSettingId) {

        WaitingSetting waitingSetting = waitingSettingRepository.findByIdOrElseThrow(waitingSettingId);

        return WaitingSettingResponseDto.toDto(waitingSetting);
    }


    /**
     * 웨이팅 설정 삭제 메서드
     *
     * @param waitingSettingId
     * @param userId
     */
    public void deleteSettingService(Long waitingSettingId, Long userId) {

        boolean isExistSetting = waitingSettingRepository
                .existsByWaitingSettingIdAndStoreUserId(waitingSettingId, userId);

        // 자신의 가게인지 확인
        if (!isExistSetting) {
            throw new UnauthorizedException(ErrorCode.NO_STORE_OWNER);
        }

        // 삭제
        waitingSettingRepository.deleteById(waitingSettingId);
    }

    /**
     * 웨이팅 상태 자동 상태 변경(to OPEN) 메서드
     *
     * @param waitingSettingId
     */
    public void updateWaitingSettingStatus(Long waitingSettingId) {
        WaitingSetting waitingSetting = waitingSettingRepository.findByIdOrElseThrow(waitingSettingId);

        waitingSetting.updateStatus(WaitingSettingStatus.OPEN);

        waitingSettingRepository.save(waitingSetting);
    }

    /**
     * 웨이팅 상태 수동 변경
     *
     * @param waitingSettingId
     * @param userId
     * @return WaitingSettingUpdateStatusResponseDto
     */
    public WaitingSettingUpdateStatusResponseDto updateWaitingSettingStatusManually(Long waitingSettingId, Long userId) {
        boolean isExistSetting = waitingSettingRepository
                .existsByWaitingSettingIdAndStoreUserId(waitingSettingId, userId);

        // 자신의 가게인지 확인
        if (!isExistSetting) {
            throw new UnauthorizedException(ErrorCode.NO_STORE_OWNER);
        }

        //가게 웨이팅 상태 ON<->OFF
        WaitingSetting waitingSetting = waitingSettingRepository.findByIdOrElseThrow(waitingSettingId);

        waitingSetting.updateStatusManually(waitingSetting);

        waitingSettingRepository.save(waitingSetting);

        return WaitingSettingUpdateStatusResponseDto.toDto(waitingSetting);
    }
}
