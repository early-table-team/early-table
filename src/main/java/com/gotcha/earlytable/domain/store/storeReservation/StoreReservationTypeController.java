package com.gotcha.earlytable.domain.store.storeReservation;

import com.gotcha.earlytable.domain.store.dto.StoreReservationTypeRequestDto;
import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import com.gotcha.earlytable.global.enums.Auth;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stores/{storeId}/reservations/type")
public class StoreReservationTypeController {

    private final StoreReservationTypeService storeReservationTypeService;

    public StoreReservationTypeController(StoreReservationTypeService storeReservationTypeService) {
        this.storeReservationTypeService = storeReservationTypeService;
    }

    /**
     * 가게 예약 타입 설정 API
     *
     * @param storeId
     * @param requestDto
     * @return ResponseEntity<String>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @PostMapping
    public ResponseEntity<String> createStoreReservationType(@PathVariable Long storeId,
                                                             @Valid @RequestBody StoreReservationTypeRequestDto requestDto,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {

        storeReservationTypeService.createStoreReservationType(storeId, userDetails.getUser().getId(), requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body("가게 예약 타입이 설정되었습니다.");
    }


    /**
     * 가게 예약 타입 제거 API
     *
     * @param storeId
     * @param requestDto
     * @param userDetails
     * @return ResponseEntity<String>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @DeleteMapping
    public ResponseEntity<Void> deleteStoreReservationType(@PathVariable Long storeId,
                                                             @Valid @RequestBody StoreReservationTypeRequestDto requestDto,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {

        storeReservationTypeService.deleteStoreReservationType(storeId, userDetails.getUser().getId(), requestDto);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
