package com.gotcha.earlytable.domain.store.storeRest;

import com.gotcha.earlytable.domain.store.dto.StoreRestRequestDto;
import com.gotcha.earlytable.domain.store.dto.StoreRestResponseDto;
import com.gotcha.earlytable.domain.store.dto.StoreRestSearchRequestDto;
import com.gotcha.earlytable.domain.store.dto.StoreRestUpdateRequestDto;
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
@RequestMapping("/rest")
public class StoreRestController {

    private final StoreRestService storeRestService;

    public StoreRestController(StoreRestService storeRestService) {
        this.storeRestService = storeRestService;
    }

    /**
     * 가게 휴무일 등록 API
     *
     * @param storeId
     * @param requestDto
     * @param userDetails
     * @return ResponseEntity<StoreRestResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @PostMapping("/stores/{storeId}")
    public ResponseEntity<StoreRestResponseDto> createStoreRest(@PathVariable Long storeId,
                                                                @Valid @RequestBody StoreRestRequestDto requestDto,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {

        StoreRestResponseDto responseDto = storeRestService.createStoreRest(storeId, userDetails.getUser().getId(), requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * 가게 휴무일 단건 조회 API
     *
     * @param restId
     * @return ResponseEntity<StoreRestResponseDto>
     */
    @GetMapping("/{restId}")
    public ResponseEntity<StoreRestResponseDto> getStoreRest(@PathVariable Long restId) {

        StoreRestResponseDto responseDto = storeRestService.getStoreRest(restId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * 가게 휴무일 조건 조회 API
     *
     * @param storeId
     * @param requestDto
     * @return ResponseEntity<List<StoreRestResponseDto>>
     */
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<List<StoreRestResponseDto>> getStoreRests(@PathVariable Long storeId,
                                                                    @Valid @RequestBody StoreRestSearchRequestDto requestDto) {

        List<StoreRestResponseDto> responseDtoList = storeRestService.getAllStoreRest(storeId, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    /**
     * 가게 휴무일 수정 API
     *
     * @param restId
     * @param requestDto
     * @param userDetails
     * @return ResponseEntity<StoreRestResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @PatchMapping("/{restId}")
    public ResponseEntity<StoreRestResponseDto> updateStoreRest(@PathVariable Long restId,
                                                                @Valid @RequestBody StoreRestUpdateRequestDto requestDto,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {

        StoreRestResponseDto responseDto = storeRestService.updateStoreRest(restId, userDetails.getUser().getId(), requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * 가게 휴무일 삭제 API
     *
     * @param restId
     * @param userDetails
     * @return ResponseEntity<Void>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @DeleteMapping("/{restId}")
    public ResponseEntity<Void> deleteStoreRest(@PathVariable Long restId,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {

        storeRestService.deleteStoreRest(restId, userDetails.getUser().getId());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
