package com.gotcha.earlytable.domain.store;

import jakarta.annotation.PostConstruct;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class StoreViewService {

    private final RedissonClient redissonClient;
    private final RTopic redissonTopic;
    private static final String PREFIX = "store:view:";
    private static final long EXPIRATION_TIME_SECONDS = 600; // 1시간 TTL

    public StoreViewService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
        // Redis Topic 설정
        this.redissonTopic = redissonClient.getTopic("store:view:count");
    }

    // 조회 시작 (조회 수 증가)
    public void publishStartViewingEvent(String storeId) {
        // 조회 수 증가 이벤트 발행
        redissonTopic.publish("start:" + storeId);
    }

    // 조회 종료 (조회 수 감소)
    public void publishStopViewingEvent(String storeId) {
        // 조회 수 감소 이벤트 발행
        redissonTopic.publish("stop:" + storeId);
    }

    // 현재 조회 수 반환
    public Long getCurrentViewCount(String storeId) {
        RAtomicLong viewCounter = redissonClient.getAtomicLong(PREFIX + storeId);

        // 만료시간 설정 - 10분
        viewCounter.expire(Duration.ofSeconds(EXPIRATION_TIME_SECONDS));

        return viewCounter.get(); // 동기적으로 조회 수 가져오기
    }

    // 실시간 조회 수 변경을 구독하는 메소드
    public void subscribeToViewEvents() {
        redissonTopic.addListener(String.class, (channel, msg) -> {
            // 채널에서 발행된 메시지를 처리
            String[] parts = msg.split(":");
            if (parts.length == 2) {
                String storeId = parts[1];
                RAtomicLong viewCounter = redissonClient.getAtomicLong(PREFIX + storeId);

                // 만료시간 설정 - 10분
                viewCounter.expire(Duration.ofSeconds(EXPIRATION_TIME_SECONDS));

                if (parts[0].equals("start")) {
                    viewCounter.incrementAndGet(); // 조회 수 증가
                } else if (parts[0].equals("stop")) {
                    if(viewCounter.get() > 0) {
                        viewCounter.decrementAndGet(); // 조회 수 감소
                    }
                }
            }
        });
    }


    @PostConstruct
    public void init() {
        subscribeToViewEvents();
    }
}