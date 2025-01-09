package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.store.dto.CreateStoreTableRequestDto;
import com.gotcha.earlytable.domain.store.dto.StoreTableGetAllResponseDto;
import com.gotcha.earlytable.domain.store.dto.UpdateStoreTableRequestDto;
import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.enums.Auth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     *  스토어테이블 정보변경 API
     * @param storeId
     * @param storeTableId
     * @param requestDto
     * @return  ResponseEntity<String>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @PutMapping("/store/{storeId}/tables/{storeTableId}")
    public ResponseEntity<String> updateStoreTable(@PathVariable Long storeId, @PathVariable Long storeTableId, @RequestBody UpdateStoreTableRequestDto requestDto){

        storeTableService.updateStoreTable(storeId, storeTableId, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body("자리정보가 변경되었습니다.");
    }

    /**
     *  가게 내 모든 테이블 조회 API
     * @param storeId
     * @return
     */
    @GetMapping("/store/{storeId}/tables}")
    public ResponseEntity<StoreTableGetAllResponseDto> getAllStoreTable(@PathVariable Long storeId){

        StoreTableGetAllResponseDto responseDto = storeTableService.getAllStoreTable(storeId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


}


