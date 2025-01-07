package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.store.dto.StoreCreateRequestDto;
import com.gotcha.earlytable.domain.store.dto.StoreResponseDto;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import com.gotcha.earlytable.global.enums.Auth;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @param userDetails
     * @return ResponseEntity<StoreResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @PostMapping
    public ResponseEntity<StoreResponseDto> createStore(@Valid @ModelAttribute StoreCreateRequestDto requestDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 로그인된 유저 정보 가져오기
        User user = userDetails.getUser();

        // 생성 후 정보 받기
        StoreResponseDto storeResponseDto = storeService.createStore(user, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(storeResponseDto);
    }
}
