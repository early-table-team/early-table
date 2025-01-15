package com.gotcha.earlytable.domain.keyword;

import com.gotcha.earlytable.domain.keyword.dto.StoreKeywordRequestDto;
import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.enums.Auth;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class StoreKeywordController {

    private final StoreKeywordService storeKeywordService;

    public StoreKeywordController(StoreKeywordService storeKeywordService) {
        this.storeKeywordService = storeKeywordService;
    }

    /**
     * 가게키워드 등록 API
     *
     * @param requestDto
     * @return
     */
    @CheckUserAuth(requiredAuthorities = {Auth.ADMIN})
    @PostMapping("/stores/keywords")
    public ResponseEntity<String> createStoreKeywords(@Valid @RequestBody StoreKeywordRequestDto requestDto) {

        storeKeywordService.createStoreKeywords(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 가게키워드 삭제 API
     *
     * @param requestDto
     * @return
     */
    @CheckUserAuth(requiredAuthorities = {Auth.ADMIN})
    @DeleteMapping("/stores/keywords")
    public ResponseEntity<String> deleteStoreKeywords(@Valid @RequestBody StoreKeywordRequestDto requestDto) {

        storeKeywordService.deleteStoreKeywords(requestDto);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
