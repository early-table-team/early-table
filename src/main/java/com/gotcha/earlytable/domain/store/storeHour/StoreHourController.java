package com.gotcha.earlytable.domain.store.storeHour;

import com.gotcha.earlytable.domain.store.dto.StoreHourRequestDto;
import com.gotcha.earlytable.domain.store.dto.StoreHourResponseDto;
import com.gotcha.earlytable.domain.store.dto.StoreHourUpdateRequestDto;
import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import com.gotcha.earlytable.global.enums.Auth;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class StoreHourController {

    private final StoreHourService storeHourService;

    public StoreHourController(StoreHourService storeHourService) {
        this.storeHourService = storeHourService;
    }

    /**
     * 가게 영업시간 등록 API
     *
     * @param storeId
     * @param requestDto
     * @return ResponseEntity<WaitingSettingResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.ADMIN, Auth.OWNER})
    @PostMapping("/stores/{storeId}/hours")
    public ResponseEntity<StoreHourResponseDto> createStoreHour(@PathVariable Long storeId,
                                                                @Valid @RequestBody StoreHourRequestDto requestDto,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {

        StoreHourResponseDto responseDto =
                storeHourService.createStoreHour(storeId, userDetails.getUser(), requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * 가게 영업시간 수정 API
     *
     * @param storeHourId
     * @param requestDto
     * @param userDetails
     * @return ResponseEntity<WaitingSettingResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.ADMIN, Auth.OWNER})
    @PutMapping("/hours/{storeHourId}")
    public ResponseEntity<StoreHourResponseDto> updateStoreHour(@PathVariable Long storeHourId,
                                                                @Valid @RequestBody StoreHourUpdateRequestDto requestDto,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {

        StoreHourResponseDto responseDto = storeHourService.updateStoreHour(storeHourId, userDetails.getUser(), requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
