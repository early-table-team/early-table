package com.gotcha.earlytable.domain.waitingsetting;

import com.gotcha.earlytable.domain.waitingsetting.dto.WaitingSettingRequestDto;
import com.gotcha.earlytable.domain.waitingsetting.dto.WaitingSettingResponseDto;
import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import com.gotcha.earlytable.global.enums.Auth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class WaitingSettingController {

    private final WaitingSettingService waitingSettingService;


    public WaitingSettingController(WaitingSettingService waitingSettingService) {
        this.waitingSettingService = waitingSettingService;
    }

    /**
     * 웨이팅 설정 등록 API
     *
     * @param storeId
     * @param requestDto
     * @param userDetails
     * @return ResponseEntity<WaitingSettingResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @PostMapping("/stores/{storeId}/waiting/settings")
    public ResponseEntity<WaitingSettingResponseDto> createWaitingSetting(@PathVariable Long storeId,
                                                                          @RequestBody WaitingSettingRequestDto requestDto,
                                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {

        WaitingSettingResponseDto responseDto = waitingSettingService.createWaitingSetting(storeId,
                userDetails.getUser().getId(), requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    /**
     * 웨이팅 설정 변경 API
     *
     * @param waitingSettingId
     * @param requestDto
     * @param userDetails
     * @return ResponseEntity<WaitingSettingResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @PutMapping("/waiting/settings/{waitingSettingId}")
    public ResponseEntity<WaitingSettingResponseDto> updateWaitingSetting(@PathVariable Long waitingSettingId,
                                                                          @Valid @RequestBody WaitingSettingUpdateRequestDto requestDto,
                                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {

        WaitingSettingResponseDto responseDto = waitingSettingService.updateWaitingSetting(waitingSettingId,
                userDetails.getUser().getId(), requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * 웨이팅 상태 수동 변경 API
     * @param waitingSettingId
     * @param userDetails
     * @return WaitingSettingUpdateStatusResponseDto
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @PatchMapping("/waiting/settings/{waitingSettingId}")
    public ResponseEntity<WaitingSettingUpdateStatusResponseDto> updateWaitingSettingStatus (@PathVariable Long waitingSettingId,
                                                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        WaitingSettingUpdateStatusResponseDto updateStatusResponseDto = waitingSettingService.updateWaitingSettingStatusManually(waitingSettingId, userDetails.getUser().getId());

        return ResponseEntity.status(HttpStatus.OK).body(updateStatusResponseDto);
    }

    /**
     * 웨이팅 설정 조회 API
     *
     * @param waitingSettingId
     * @return ResponseEntity<WaitingSettingResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @GetMapping("/waiting/settings/{waitingSettingId}")
    public ResponseEntity<WaitingSettingResponseDto> getWaitingSetting(@PathVariable Long waitingSettingId) {

        WaitingSettingResponseDto responseDto = waitingSettingService.getWaitingSetting(waitingSettingId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    /**
     * 웨이팅 설정 삭제 API
     *
     * @param waitingSettingId
     * @param userDetails
     * @return ResponseEntity<Void>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @DeleteMapping("/waiting/settings/{waitingSettingId}")
    public ResponseEntity<Void> deleteWaitingSetting(@PathVariable Long waitingSettingId,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {

        waitingSettingService.deleteSettingService(waitingSettingId, userDetails.getUser().getId());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @GetMapping("stores/{storeId}/waiting/settings")
    public ResponseEntity<WaitingSettingResponseDto> getWaitingSettingByStoreId(@PathVariable Long storeId) {

        WaitingSettingResponseDto responseDto = waitingSettingService.getWaitingSettingByStoreId(storeId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
