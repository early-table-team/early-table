package com.gotcha.earlytable.domain.waiting;

import com.gotcha.earlytable.domain.waiting.dto.WaitingOfflineRequestDto;
import com.gotcha.earlytable.domain.waiting.dto.WaitingOfflineResponseDto;
import com.gotcha.earlytable.domain.waiting.dto.WaitingOnlineRequestDto;
import com.gotcha.earlytable.domain.waiting.dto.WaitingOnlineResponseDto;
import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.enums.Auth;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WaitingController {

    WaitingService waitingService;

    /**
     * 웨이팅 생성 API
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

    @PostMapping("stores/{storeId}/waiting/offline")
    public ResponseEntity<WaitingOfflineResponseDto> creatWaitingOffline(@Valid @RequestBody WaitingOfflineRequestDto requestDto,
                                                                        @PathVariable Long storeId) {

        WaitingOfflineResponseDto responseDto = waitingService.creatWaitingOffline(requestDto, storeId);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}

