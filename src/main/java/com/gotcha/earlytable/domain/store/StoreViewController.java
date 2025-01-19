package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stores")
public class StoreViewController {

    private final StoreViewService storeViewService;

    public StoreViewController(StoreViewService storeViewService) {
        this.storeViewService = storeViewService;
    }

    /**
     * 해당 가게를 조회하면 이벤트 처리로 호출되는 API
     * 현재 가게를 보고있는 사람 수 ++
     *
     * @param storeId
     * @return 가게 접속자 수 조회 시작 메세지
     */
    @PostMapping("/{storeId}/view/start")
    public ResponseEntity<String> startViewing(@PathVariable Long storeId,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 조회 시작 이벤트 발행
        storeViewService.publishStartViewingEvent(storeId, userDetails.getUser().getId());

        return ResponseEntity.ok("View started for store: " + storeId);
    }

    /**
     * 해당 가게 조회에서 나가면 이벤트 처리로 호출되는 API
     * 현재 가게를 보고있는 사람 수 --
     *
     * @param storeId
     * @return 가게 접속자 수 조회 멈춤 메세지
     */
    @PostMapping("/{storeId}/view/stop")
    public ResponseEntity<String> stopViewing(@PathVariable Long storeId,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 조회 종료 이벤트 발행
        storeViewService.publishStopViewingEvent(storeId, userDetails.getUser().getId());

        return ResponseEntity.ok("View stopped for store: " + storeId);
    }

    /**
     * 실시간 접속자 수 스트리밍
     *
     * @param storeId
     * @return 접속자 수
     */
    @GetMapping(value = "/{storeId}/view/stream", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> streamViewCount(@PathVariable Long storeId) {
        // 현재 조회 수를 Redis에서 가져옴
        Long count = storeViewService.getCurrentViewCount(storeId);
        return ResponseEntity.ok(count);
    }
}