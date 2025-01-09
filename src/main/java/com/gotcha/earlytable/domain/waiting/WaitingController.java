package com.gotcha.earlytable.domain.waiting;

import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.domain.waiting.dto.*;
import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import com.gotcha.earlytable.global.enums.Auth;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WaitingController {

    private final WaitingRepository waitingRepository;
    WaitingService waitingService;

    public WaitingController(WaitingRepository waitingRepository) {
        this.waitingRepository = waitingRepository;
    }

    /**
     * 원격 웨이팅 생성 API
     *
     * @param requestDto
     * @param storeId
     * @return ResponseEntity
     */

    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @PostMapping("/stores/{storeId}/waiting/online")
    public ResponseEntity<WaitingOnlineResponseDto> createWaitingOnline(@Valid @RequestBody WaitingOnlineRequestDto requestDto,
                                                                        @PathVariable Long storeId,
                                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        WaitingOnlineResponseDto responseDto = waitingService.createWaitingOnline(requestDto, storeId, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * 현장 웨이팅 생성 API
     *
     * @param requestDto
     * @param storeId
     * @return
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
     * @return
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @GetMapping("/waiting")
    public ResponseEntity<List<WaitingListResponseDto>> getWaitingList(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        // 로그인된 유저 정보 가져오기
        User user = userDetails.getUser();

        List<WaitingListResponseDto> responseDtos = waitingService.getWaitingList(user);

        return ResponseEntity.status(HttpStatus.OK).body(responseDtos);
    }

    /**
     * 웨이팅 미루기
     *
     * @param userDetails
     * @param waitingId
     * @return
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
     * @return
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER, Auth.OWNER})
    @GetMapping("/waiting/{waitingId}")
    public ResponseEntity<WaitingDetailResponseDto> getWaitingDetail(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                     @PathVariable Long waitingId) {

        User user = userDetails.getUser();

        WaitingDetailResponseDto responseDto = waitingService.getWaitingDetail(waitingId, user);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * 웨이팅 취소 API
     *
     * @param userDetails
     * @param waitingId
     * @return
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @DeleteMapping("/waiting/{waitingId}")
    public ResponseEntity<String> cancelWaiting(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                @PathVariable Long waitingId) {
        User user = userDetails.getUser();

        waitingService.cancelWaiting(waitingId, user);

        return ResponseEntity.status(HttpStatus.OK).body("웨이팅이 취소되었습니다");
    }




}

