package com.gotcha.earlytable.domain.waitingsetting;

import com.gotcha.earlytable.domain.store.StoreRepository;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.waitingsetting.dto.WaitingSettingRequestDto;
import com.gotcha.earlytable.domain.waitingsetting.dto.WaitingSettingResponseDto;
import com.gotcha.earlytable.domain.waitingsetting.entity.WaitingSetting;
import com.gotcha.earlytable.global.error.ErrorCode;
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
    public WaitingSettingResponseDto createWaitingSetting(Long storeId, Long userId, WaitingSettingRequestDto requestDto) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 자신의 가게인지 확인
        if (store.getUser().getId().equals(userId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
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
                                                          WaitingSettingRequestDto requestDto) {

        WaitingSetting waitingSetting = waitingSettingRepository.findByIdOrElseThrow(waitingSettingId);

        // 자신의 가게인지 확인
        if (waitingSetting.getStore().getUser().getId().equals(userId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
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
        if (isExistSetting) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }

        // 삭제
        waitingSettingRepository.deleteById(waitingSettingId);
    }
}
