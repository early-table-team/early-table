package com.gotcha.earlytable.domain.waiting;

import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.domain.waiting.dto.*;
import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import com.gotcha.earlytable.global.enums.Auth;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WaitingController {

    WaitingService waitingService;

    /**
     * 원격 웨이팅 생성 API
     *
     * @param requestDto
     * @param storeId
     * @return ResponseEntity
     */

    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @PostMapping("stores/{storeId}/waiting/online")
    public ResponseEntity<WaitingOnlineResponseDto> creatWaitingOnline(@Valid @RequestBody WaitingOnlineRequestDto requestDto,
                                                                       @PathVariable Long storeId) {

        WaitingOnlineResponseDto responseDto = waitingService.creatWaitingOnline(requestDto, storeId);

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
    public ResponseEntity<WaitingNumberResponseDto> creatWaitingOffline(@Valid @RequestBody WaitingOfflineRequestDto requestDto,
                                                                        @PathVariable Long storeId) {

        WaitingNumberResponseDto responseDto = waitingService.creatWaitingOffline(requestDto, storeId);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    /**
     * 웨이팅 목록 조회 API
     *
     * @param userDetails
     * @return
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @GetMapping("waiting")
    public ResponseEntity<List<WaitingListResponseDto>> getWaiting(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        // 로그인된 유저 정보 가져오기
        User user = userDetails.getUser();

        List<WaitingListResponseDto> responseDtos = waitingService.getWaitingList(user);

        return ResponseEntity.status(HttpStatus.OK).body(responseDtos);
    }

}

