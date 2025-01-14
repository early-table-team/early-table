package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.store.dto.TimeSlotRequestDto;
import com.gotcha.earlytable.domain.store.dto.TimeSlotResponseDto;
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
@RequestMapping("/stores/{storeId}/storeTimeSlots")
public class StoreTimeSlotController {

    private final StoreTimeSlotService storeTimeSlotService;

    public StoreTimeSlotController(StoreTimeSlotService storeTimeSlotService) {
        this.storeTimeSlotService = storeTimeSlotService;
    }

    /**
     * 타임슬롯 생성 API
     *
     * @param storeId
     * @param requestDto
     * @param userDetails
     * @return ResponseEntity<ReservationMasterResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @PostMapping
    public ResponseEntity<TimeSlotResponseDto> createStoreTimeSlot(@PathVariable Long storeId,
                                                                   @Valid @RequestBody TimeSlotRequestDto requestDto,
                                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {

        TimeSlotResponseDto responseDto = storeTimeSlotService.createStoreTimeSlot(storeId, requestDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * 타임슬롯 전체조회 API
     *
     * @param storeId
     * @return ResponseEntity<List<TimeSlotResponseDto>>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @GetMapping
    public ResponseEntity<List<TimeSlotResponseDto>> getAllTimeSlots(@PathVariable Long storeId) {

        List<TimeSlotResponseDto> responseDto = storeTimeSlotService.getAllTimeSlots(storeId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * 타임슬롯 단일 조회 API
     *
     * @param storeTimeSlotId
     * @return ResponseEntity<TimeSlotResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @GetMapping("/{storeTimeSlotId}")
    public ResponseEntity<TimeSlotResponseDto> getOneTimeSlot(@PathVariable Long storeTimeSlotId,
                                                              @PathVariable Long storeId) {

        TimeSlotResponseDto responseDto = storeTimeSlotService.getOneTimeSlot(storeTimeSlotId, storeId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }

    /**
     * 타임슬롯 수정 API
     *
     * @param storeId
     * @param storeTimeSlotId
     * @param requestDto
     * @param userDetails
     * @return ResponseEntity<TimeSlotResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @PutMapping("/{storeTimeSlotId}")
    public ResponseEntity<TimeSlotResponseDto> modifyTimeSlot(@PathVariable Long storeId,
                                                              @PathVariable Long storeTimeSlotId,
                                                              @Valid @RequestBody TimeSlotRequestDto requestDto,
                                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {

        TimeSlotResponseDto responseDto =
                storeTimeSlotService.modifyTimeSlot(storeId, storeTimeSlotId, requestDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * 타임슬롯 삭제 API
     *
     * @param storeId
     * @param storeTimeSlotId
     * @param userDetails
     * @return ResponseEntity<String>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @DeleteMapping("/{storeTimeSlotId}")
    public ResponseEntity<String> deleteTimeSlot(@PathVariable Long storeId,
                                                 @PathVariable Long storeTimeSlotId,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {

        storeTimeSlotService.deleteTimeSlot(storeId, storeTimeSlotId, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("타임슬롯이 삭제되었습니다.");
    }


}
