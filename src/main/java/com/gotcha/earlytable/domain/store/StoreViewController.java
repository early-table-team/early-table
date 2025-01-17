package com.gotcha.earlytable.domain.store;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/store")
public class StoreViewController {

    private final StoreViewServiceFromRedisson storeViewService;

    public StoreViewController(StoreViewServiceFromRedisson storeViewService) {
        this.storeViewService = storeViewService;
    }

    @PostMapping("/{storeId}/view/start")
    public CompletableFuture<ResponseEntity<String>> startViewing(@PathVariable String storeId) {
        return storeViewService.startViewingAsync(storeId)
                .thenApply(count -> ResponseEntity.ok("View started for store: " + storeId + ", Count: " + count));
    }

    @PostMapping("/{storeId}/view/stop")
    public CompletableFuture<ResponseEntity<String>> stopViewing(@PathVariable String storeId) {
        return storeViewService.stopViewingAsync(storeId)
                .thenApply(count -> ResponseEntity.ok("View stopped for store: " + storeId + ", Count: " + count));
    }

    @GetMapping("/{storeId}/view/count")
    public CompletableFuture<ResponseEntity<Long>> getCurrentViewCount(@PathVariable String storeId) {
        return storeViewService.getCurrentViewCountAsync(storeId)
                .thenApply(ResponseEntity::ok);
    }
}