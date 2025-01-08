package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.store.dto.StoreRequestDto;
import com.gotcha.earlytable.domain.store.dto.StoreResponseDto;
import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.enums.Auth;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
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
}
