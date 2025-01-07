package com.gotcha.earlytable.domain.waiting;

import com.gotcha.earlytable.domain.waiting.dto.WaitingRequestDto;
import com.gotcha.earlytable.domain.waiting.dto.WaitingResponseDto;
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
    @PostMapping("stores/{storeId}/waiting")
    public ResponseEntity<WaitingResponseDto> creatWaiting(@Valid @RequestBody WaitingRequestDto requestDto,
                                                           @PathVariable Long storeId) {

        WaitingResponseDto responseDto = waitingService.creatWaiting(requestDto, storeId);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}

