package com.gotcha.earlytable.domain.store;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Service
public class StoreViewService {

    private final RedissonClient redissonClient;
    private static final String PREFIX = "store:view:";

    public StoreViewService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    // 조회 시작 (조회 수 증가)
    public Long startViewing(String storeId) {
        RAtomicLong viewCounter = redissonClient.getAtomicLong(PREFIX + storeId);

        Long count = viewCounter.incrementAndGet(); // 조회 수 증가

        viewCounter.expire(Duration.ofMinutes(10)); // 동기적으로 만료 시간 설정
        return count;
    }

    // 조회 종료 (조회 수 감소)
    public long stopViewing(String storeId) {
        RAtomicLong viewCounter = redissonClient.getAtomicLong(PREFIX + storeId);

        if (viewCounter.get() > 0) {
            return viewCounter.decrementAndGet(); // 동기적으로 조회 수 감소
        }
        return 0;
    }

    // 현재 조회 수 반환
    public Long getCurrentViewCount(String storeId) {
        RAtomicLong viewCounter = redissonClient.getAtomicLong(PREFIX + storeId);

        return viewCounter.get(); // 동기적으로 조회 수 가져오기
    }

}