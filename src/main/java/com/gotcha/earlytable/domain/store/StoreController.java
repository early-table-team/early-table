package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.store.dto.*;
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
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;
    private final StoreRepository storeRepository;

    public StoreController(StoreService storeService, StoreRepository storeRepository) {
        this.storeService = storeService;
        this.storeRepository = storeRepository;
    }

    /**
     * 가게 생성 API
     *
     * @param requestDto
     * @return ResponseEntity<StoreResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.ADMIN})
    @PostMapping
    public ResponseEntity<StoreResponseDto> createStore(@Valid @ModelAttribute StoreRequestDto requestDto) {

        // 생성 후 정보 받기
        StoreResponseDto storeResponseDto = storeService.createStore(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(storeResponseDto);
    }

    /**
     * 가게 수정 API
     *
     * @param storeId
     * @param requestDto
     * @return ResponseEntity<StoreResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.ADMIN})
    @PutMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> updateStore(@PathVariable Long storeId,
                                                        @Valid @ModelAttribute StoreRequestDto requestDto) {

        // 생성 후 정보 받기
        StoreResponseDto storeResponseDto = storeService.updateStore(storeId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(storeResponseDto);
    }

    /**
     * 가게 단건 조회 API
     *
     * @param storeId
     * @return ResponseEntity<StoreResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER, Auth.USER})
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> getStore(@PathVariable Long storeId) {

        StoreResponseDto storeResponseDto = storeService.getStore(storeId);

        return ResponseEntity.status(HttpStatus.OK).body(storeResponseDto);
    }

    /**
     * 나의 가게 전체 조회 API
     *
     * @param userDetails
     * @return ResponseEntity<List<StoreListResponseDto>>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @GetMapping
    public ResponseEntity<List<StoreListResponseDto>> getMyStores(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<StoreListResponseDto> storeListResponseDtoList = storeService.getStores(userDetails.getUser().getId());

        return ResponseEntity.status(HttpStatus.OK).body(storeListResponseDtoList);
    }


    /**
     * 가게 휴업 상태 <-> 영업 상태 변경 API
     *
     * @param storeId
     * @param userDetails
     * @return ResponseEntity<String>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @PatchMapping("/{storeId}/status/rest")
    public ResponseEntity<String> storeStatusRest( @PathVariable Long storeId,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String message = storeService.updateStoreStatus(storeId, userDetails.getUser().getId());

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    /**
     * 가게 상태 변경 (Admin)
     *
     * @param storeId
     * @return ResponseEntity<String>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.ADMIN})
    @PatchMapping("/{storeId}/status")
    public ResponseEntity<String> updateStoreStatus( @PathVariable Long storeId,
                                                     @RequestBody StoreStatusRequestDto requestDto) {

        storeService.updateStoreStatus(storeId, requestDto.getStoreStatus());

        return ResponseEntity.status(HttpStatus.OK).body("가게 상태 변경이 완료되었습니다.");
    }

    /**
     * 가게 검색 조회
     *
     * @param requestDto
     * @return
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @GetMapping("/search")
    public ResponseEntity<List<StoreListResponseDto>> searchStore(@RequestBody StoreSearchRequestDto requestDto) {

        List<StoreListResponseDto> responseDtoList = storeService.searchStore(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }
}
