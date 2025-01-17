package com.gotcha.earlytable.domain.waiting;

import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.domain.waiting.dto.*;
import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import com.gotcha.earlytable.global.enums.Auth;
import com.gotcha.earlytable.global.enums.WaitingStatus;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WaitingController {

    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    /**
     * 원격 웨이팅 생성 API
     *
     * @param requestDto
     * @param storeId
     * @return ResponseEntity<WaitingResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @PostMapping("/stores/{storeId}/waiting/online")
    public ResponseEntity<WaitingResponseDto> createWaitingOnline(@Valid @RequestBody WaitingOnlineRequestDto requestDto,
                                                                        @PathVariable Long storeId,
                                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        WaitingResponseDto responseDto = waitingService.createWaitingOnline(requestDto, storeId, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * 현장 웨이팅 생성 API
     *
     * @param requestDto
     * @param storeId
     * @return ResponseEntity<WaitingNumberResponseDto>
     */
    @PostMapping("/stores/{storeId}/waiting/offline")
    public ResponseEntity<WaitingNumberResponseDto> createWaitingOffline(@Valid @RequestBody WaitingOfflineRequestDto requestDto,
                                                                         @PathVariable Long storeId) {

        WaitingNumberResponseDto responseDto = waitingService.createWaitingOffline(requestDto, storeId);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    /**
     * 웨이팅 목록 조회 API
     *
     * @param userDetails
     * @return ResponseEntity<List < WaitingListResponseDto>>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @GetMapping("/waiting")
    public ResponseEntity<List<WaitingResponseDto>> getWaitingList(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        // 로그인된 유저 정보 가져오기
        User user = userDetails.getUser();

        List<WaitingResponseDto> responseDtoList = waitingService.getWaitingList(user);

        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    /**
     * 현재 남은 웨이팅 목록 조회 (Owner)
     *
     * @param userDetails
     * @param storeId
     * @param requestDto
     * @return
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @GetMapping("/stores/{storeId}/waiting/now")
    public ResponseEntity<WaitingOwnerResponseDto> getOwnerNowWaitingList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                       @PathVariable Long storeId,
                                                                       @Valid @RequestBody WaitingSimpleOwnerRequestDto requestDto) {

        // 로그인된 유저 정보 가져오기
        User user = userDetails.getUser();

        WaitingOwnerResponseDto responseDto = waitingService.getOwnerNowWaitingList(user, storeId, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * 웨이팅 목록 조회
     *
     * @param userDetails
     * @param storeId
     * @param requestDto
     * @return
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @GetMapping("/stores/{storeId}/waiting")
    public ResponseEntity<WaitingOwnerResponseDto> getOwnerWaitingList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                       @PathVariable Long storeId,
                                                                       @Valid @RequestBody WaitingOwnerRequestDto requestDto) {

        // 로그인된 유저 정보 가져오기
        User user = userDetails.getUser();

        WaitingOwnerResponseDto responseDto = waitingService.getOwnerWaitingList(user, storeId, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * 웨이팅 미루기
     *
     * @param userDetails
     * @param waitingId
     * @return ResponseEntity<WaitingNumberResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @PatchMapping("/waiting/{waitingId}")
    public ResponseEntity<WaitingNumberResponseDto> delayWaiting(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                 @PathVariable Long waitingId) {
        User user = userDetails.getUser();

        WaitingNumberResponseDto responseDto = waitingService.delayWaiting(waitingId, user);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * 웨이팅 상세 조회 API
     *
     * @param userDetails
     * @param waitingId
     * @return ResponseEntity<WaitingDetailResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER, Auth.OWNER})
    @GetMapping("/waiting/{waitingId}")
    public ResponseEntity<WaitingGetOneResponseDto> getWaitingDetail(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                     @PathVariable Long waitingId) {

        User user = userDetails.getUser();

        WaitingGetOneResponseDto responseDto = waitingService.getWaitingDetail(waitingId, user);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * 웨이팅 취소 API
     *
     * @param userDetails
     * @param waitingId
     * @return ResponseEntity<String>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER, Auth.USER})
    @DeleteMapping("/waiting/{waitingId}/status")
    public ResponseEntity<String> cancelWaiting(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                @PathVariable Long waitingId) {
        User user = userDetails.getUser();

        waitingService.cancelWaiting(waitingId, user);

        return ResponseEntity.status(HttpStatus.OK).body("웨이팅이 취소되었습니다");
    }

    /**
     * 웨이팅 입장 완료 API
     *
     * @param waitingId
     * @return ResponseEntity<String>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @PatchMapping("/waiting/{waitingId}/status")
    public ResponseEntity<String> completeWaitingStatus(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @PathVariable Long waitingId) {

        waitingService.changeWaitingStatus(waitingId, WaitingStatus.COMPLETED, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body("입장 완료 되었습니다.");
    }


    /**
     * 현재 웨이팅 순서 조회 API
     *
     * @param waitingId
     * @return ResponseEntity<WaitingNumberResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @GetMapping("/waiting/{waitingId}/now")
    public ResponseEntity<WaitingNumberResponseDto> getNowSeqNumber(@PathVariable Long waitingId) {

        WaitingNumberResponseDto responseDto = waitingService.getNowSeqNumber(waitingId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
