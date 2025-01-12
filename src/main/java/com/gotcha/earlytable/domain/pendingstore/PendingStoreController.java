package com.gotcha.earlytable.domain.pendingstore;

import com.gotcha.earlytable.domain.pendingstore.dto.*;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import com.gotcha.earlytable.global.enums.Auth;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pending-stores")
public class PendingStoreController {

    private final PendingStoreService pendingStoreService;

    public PendingStoreController(PendingStoreService pendingStoreService) {
        this.pendingStoreService = pendingStoreService;
    }

    /**
     * 가게 생성 요청 API
     *
     * @param requestDto
     * @param userDetails
     * @return ResponseEntity<StoreResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @PostMapping("/pending-stores")
    public ResponseEntity<PendingStoreResponseDto> createPendingStore(@Valid @ModelAttribute PendingStoreRequestDto requestDto,
                                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {

        // 로그인된 유저 정보 가져오기
        User user = userDetails.getUser();

        // 생성 후 정보 받기
        PendingStoreResponseDto responseDto = pendingStoreService.createPendingStore(user, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * 가게 수정 요청 API
     *
     * @param storeId
     * @param requestDto
     * @return ResponseEntity<StoreResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @PutMapping("/stores/{storeId}")
    public ResponseEntity<PendingStoreResponseDto> updatePendingStore(@PathVariable Long storeId,
                                                        @Valid @ModelAttribute PendingStoreUpdateRequestDto requestDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();

        PendingStoreResponseDto responseDto = pendingStoreService.updatePendingStore(storeId, user, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * 가게 생성 및 수정 요청 전체 조회 API
     *
     * @param pageable
     * @return ResponseEntity<PendingStoreResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.ADMIN})
    @GetMapping("/pending-stores")
    public ResponseEntity<List<PendingStoreResponseListDto>> getPendingStore(@PageableDefault Pageable pageable) {

        List<PendingStoreResponseListDto> responseDtoList = pendingStoreService.getPendingStores(pageable);

        return ResponseEntity.ok(responseDtoList);
    }


    /**
     * 가게 생성 및 수정 요청 단건 조회 API
     *
     * @param pendingStoreId
     * @return ResponseEntity<PendingStoreResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.ADMIN})
    @GetMapping("/pending-stores/{pendingStoreId}")
    public ResponseEntity<PendingStoreResponseDto> getPendingStore(@PathVariable Long pendingStoreId) {

        PendingStoreResponseDto responseDto = pendingStoreService.getPendingStore(pendingStoreId);

        return ResponseEntity.ok(responseDto);
    }

    /**
     * 가게 요청 승인여부 API
     *
     * @param pendingStoreId
     * @return ResponseEntity<PendingStoreResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.ADMIN})
    @PatchMapping("/pending-stores/{pendingStoreId}/status")
    public ResponseEntity<String> updatePendingStoreStatus(@PathVariable Long pendingStoreId,
                                                           @RequestBody PendingStoreStatusRequestDto requestDto) {

        pendingStoreService.updatePendingStoreStatus(pendingStoreId, requestDto);

        return ResponseEntity.ok("가게 상태가 변경 완료되었습니다.");
    }
}
