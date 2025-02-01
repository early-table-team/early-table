package com.gotcha.earlytable.domain.store.storeReservationType;

import com.gotcha.earlytable.domain.store.dto.StoreReservationTypeCreateRequestDto;
import com.gotcha.earlytable.domain.store.dto.StoreReservationTypeDeleteRequestDto;
import com.gotcha.earlytable.domain.store.dto.StoreReservationTypeUpdateRequestDto;
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
    @CheckUserAuth(requiredAuthorities = {Auth.ADMIN, Auth.OWNER})
    @PostMapping
    public ResponseEntity<String> createStoreReservationType(@PathVariable Long storeId,
                                                             @Valid @RequestBody StoreReservationTypeCreateRequestDto requestDto,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {

        storeReservationTypeService.createStoreReservationType(storeId, userDetails.getUser(), requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body("가게 예약 타입이 설정되었습니다.");
    }

    /**
     * 가게 예약 타입 변경 API
     *
     * @param storeId
     * @param requestDto
     * @param userDetails
     * @return ResponseEntity<String>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.ADMIN, Auth.OWNER})
    @PatchMapping
    public ResponseEntity<String> updateStoreReservationType(@PathVariable Long storeId,
                                                             @Valid @RequestBody StoreReservationTypeUpdateRequestDto requestDto,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {

        storeReservationTypeService.updateStoreReservationType(storeId, userDetails.getUser(), requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body("가게 예약 타입이 변경되었습니다.");
    }


    /**
     * 가게 예약 타입 제거 API
     *
     * @param storeId
     * @param requestDto
     * @param userDetails
     * @return ResponseEntity<String>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.ADMIN, Auth.OWNER})
    @DeleteMapping
    public ResponseEntity<Void> deleteStoreReservationType(@PathVariable Long storeId,
                                                             @Valid @RequestBody StoreReservationTypeDeleteRequestDto requestDto,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {

        storeReservationTypeService.deleteStoreReservationType(storeId, userDetails.getUser(), requestDto);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<String> getOnsiteType(@PathVariable Long storeId){
        String answer = "";
       boolean response = storeReservationTypeService.getOnsiteType(storeId);
       if(response){
           answer = "true";
       }else{
           answer = "false";
       }


        return ResponseEntity.status(HttpStatus.OK).body(answer);
    }
}
