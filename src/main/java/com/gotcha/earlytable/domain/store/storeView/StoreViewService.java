package com.gotcha.earlytable.domain.store.storeView;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class StoreViewService {

    private final RedissonClient redissonClient;

    private static final String PREFIX = "store:view:";
    private static final long EXPIRATION_TIME_SECONDS = 600; // 1시간 TTL

    public StoreViewService(RedissonClient redissonClient) {

        this.redissonClient = redissonClient;
    }

    // 현재 조회 수 반환
    public Long getCurrentViewCount(Long storeId) {
        RAtomicLong viewCounter = redissonClient.getAtomicLong(PREFIX + storeId);

        // 만료시간 설정 - 10분
        viewCounter.expire(Duration.ofSeconds(EXPIRATION_TIME_SECONDS));

        return viewCounter.get(); // 동기적으로 조회 수 가져오기
    }

}