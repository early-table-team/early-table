package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import com.gotcha.earlytable.global.enums.Auth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interests/stores")
public class InterestStoreController {

    private final InterestStoreService interestStoreService;

    public InterestStoreController(InterestStoreService interestStoreService) {

        this.interestStoreService = interestStoreService;
    }


    /**
     *  관심가게 등록 API
     * @param storeId
     * @param userDetails
     * @return  ResponseEntity<String>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @PostMapping("/{storeId}")
    public ResponseEntity<String> registerStore(@PathVariable Long storeId, @AuthenticationPrincipal UserDetailsImpl userDetails){

        interestStoreService.registerStore(storeId, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body("관심가게로 등록되었습니다.");
    }



}
