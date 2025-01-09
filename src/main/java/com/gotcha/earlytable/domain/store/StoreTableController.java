package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.store.dto.CreateStoreTableRequestDto;
import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.enums.Auth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoreTableController {

    private final StoreTableService storeTableService;

    public StoreTableController(StoreTableService storeTableService) {
        this.storeTableService = storeTableService;
    }

    /**
     *  StoreTable 생성 API
     * @param storeId
     * @param requestDto
     * @return
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @PostMapping("/store/{storeId}/tables")
    public ResponseEntity<String> createStoreTable(@PathVariable Long storeId, @RequestBody CreateStoreTableRequestDto requestDto) {

        storeTableService.createStoreTable(storeId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body("자리가 생성되었습니다.");

    }
}


