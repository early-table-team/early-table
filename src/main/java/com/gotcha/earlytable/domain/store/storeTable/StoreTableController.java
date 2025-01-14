package com.gotcha.earlytable.domain.store.storeTable;

import com.gotcha.earlytable.domain.store.dto.StoreTableCreateRequestDto;
import com.gotcha.earlytable.domain.store.dto.StoreTableGetAllResponseDto;
import com.gotcha.earlytable.domain.store.dto.StoreTableUpdateRequestDto;
import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import com.gotcha.earlytable.global.enums.Auth;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stores/{storeId}/tables")
public class StoreTableController {

    private final StoreTableService storeTableService;

    public StoreTableController(StoreTableService storeTableService) {
        this.storeTableService = storeTableService;
    }

    /**
     * StoreTable 생성 API
     *
     * @param storeId
     * @param requestDto
     * @return
     */
    @CheckUserAuth(requiredAuthorities = {Auth.ADMIN, Auth.OWNER})
    @PostMapping
    public ResponseEntity<String> createStoreTable(@PathVariable Long storeId,
                                                   @Valid @RequestBody StoreTableCreateRequestDto requestDto,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {

        storeTableService.createStoreTable(storeId, requestDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body("자리가 생성되었습니다.");

    }

    /**
     * 스토어테이블 정보변경 API
     *
     * @param storeId
     * @param storeTableId
     * @param requestDto
     * @return ResponseEntity<String>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.ADMIN, Auth.OWNER})
    @PutMapping("/{storeTableId}")
    public ResponseEntity<String> updateStoreTable(@PathVariable Long storeId, @PathVariable Long storeTableId,
                                                   @Valid @RequestBody StoreTableUpdateRequestDto requestDto,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {

        storeTableService.updateStoreTable(storeId, storeTableId, requestDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body("자리정보가 변경되었습니다.");
    }

    /**
     * 가게 내 모든 테이블 조회 API
     *
     * @param storeId
     * @return ResponseEntity<StoreTableGetAllResponseDto>
     */
    @GetMapping
    public ResponseEntity<StoreTableGetAllResponseDto> getAllStoreTable(@PathVariable Long storeId) {

        StoreTableGetAllResponseDto responseDto = storeTableService.getAllStoreTable(storeId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * 가게자리정보 삭제 API
     *
     * @param storeId
     * @param storeTableId
     * @return ResponseEntity<String>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.ADMIN, Auth.OWNER})
    @DeleteMapping("/{storeTableId}")
    public ResponseEntity<Void> deleteStoreTable(@PathVariable Long storeId, @PathVariable Long storeTableId,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {

        storeTableService.deleteStoreTable(storeId, storeTableId, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}


