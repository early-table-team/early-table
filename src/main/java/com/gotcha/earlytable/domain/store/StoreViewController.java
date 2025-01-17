package com.gotcha.earlytable.domain.store;

import jakarta.servlet.http.HttpServletResponse;
import org.redisson.api.RAtomicLong;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;


@RestController
@RequestMapping("/store")
public class StoreViewController {

    private final StoreViewService storeViewService;

    public StoreViewController(StoreViewService storeViewService) {
        this.storeViewService = storeViewService;
    }

    /**
     * 해당 가게를 조회하면 이벤트 처리로 호출되는 API
     *
     * 현재 가게를 보고있는 사람 수 ++
     *
     * @param storeId
     * @return
     */
    @PostMapping("/{storeId}/view/start")
    public ResponseEntity<String> startViewing(@PathVariable String storeId) {

        long count = storeViewService.startViewing(storeId);

        return ResponseEntity.ok("View started for store: " + storeId + ", Current View Count: " + count);
    }

    /**
     * 해당 가게 조회에서 나가면 이벤트 처리로 호출되는 API
     *
     * 현재 가게를 보고있는 사람 수 --
     *
     * @param storeId
     * @return
     */
    @PostMapping("/{storeId}/view/stop")
    public ResponseEntity<String> stopViewing(@PathVariable String storeId) {

        long count = storeViewService.stopViewing(storeId);

        return ResponseEntity.ok("View stopped for store: " + storeId + ", Current View Count: " + count);
    }


    // 실시간 접속자 수 스트리밍
    @GetMapping(value = "/{storeId}/view/stream")
    public ResponseEntity<Long> streamViewCount(@PathVariable String storeId) {

        Long count = storeViewService.getCurrentViewCount(storeId);

        return ResponseEntity.ok(count);
    }

}