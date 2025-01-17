package com.gotcha.earlytable.domain.store;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RFuture;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;


@Service
public class StoreViewServiceFromRedisson {

    private final RedissonClient redissonClient;
    private static final String PREFIX = "store:view:";

    public StoreViewServiceFromRedisson(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    // 조회 시작 (조회 수 증가)
    public CompletableFuture<Long> startViewingAsync(String storeId) {
        RAtomicLong viewCounter = redissonClient.getAtomicLong(PREFIX + storeId);
        RFuture<Long> incrementFuture = viewCounter.incrementAndGetAsync();

        // RFuture -> CompletableFuture 변환 후 TTL 설정
        return toCompletableFuture(incrementFuture)
                .thenCompose(count -> {
                    RFuture<Boolean> expireFuture = viewCounter.expireAsync(Duration.ofMinutes(10));
                    return toCompletableFuture(expireFuture).thenApply(expired -> count);
                });
    }

    // 조회 종료 (조회 수 감소)
    public CompletableFuture<Long> stopViewingAsync(String storeId) {
        RAtomicLong viewCounter = redissonClient.getAtomicLong(PREFIX + storeId);
        RFuture<Long> decrementFuture = viewCounter.decrementAndGetAsync();

        // RFuture -> CompletableFuture 변환
        return toCompletableFuture(decrementFuture)
                .thenApply(count -> Math.max(count, 0)); // 음수 방지
    }

    // 현재 조회 수 반환
    public CompletableFuture<Long> getCurrentViewCountAsync(String storeId) {
        RAtomicLong viewCounter = redissonClient.getAtomicLong(PREFIX + storeId);
        RFuture<Long> getFuture = viewCounter.getAsync();

        // RFuture -> CompletableFuture 변환
        return toCompletableFuture(getFuture);
    }

    // RFuture -> CompletableFuture 변환 메서드
    private <T> CompletableFuture<T> toCompletableFuture(RFuture<T> rFuture) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        rFuture.whenComplete((result, throwable) -> {
            if (throwable != null) {
                completableFuture.completeExceptionally(throwable); // 예외 처리
            } else {
                completableFuture.complete(result); // 성공 처리
            }
        });
        return completableFuture;
    }
}