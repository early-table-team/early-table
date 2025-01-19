package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.notification.SseEmitterRepository;
import com.gotcha.earlytable.domain.notification.SseEmitterService;
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
    private final SseEmitterService sseEmitterService;

    private static final String PREFIX = "store:view:";
    private static final long EXPIRATION_TIME_SECONDS = 600; // 1시간 TTL
    private final SseEmitterRepository sseEmitterRepository;

    public StoreViewService(RedissonClient redissonClient, SseEmitterService sseEmitterService, SseEmitterRepository sseEmitterRepository) {

        // Redis Topic 설정
        this.redissonTopic = redissonClient.getTopic("store:view:count");
        this.redissonClient = redissonClient;
        this.sseEmitterService = sseEmitterService;
        this.sseEmitterRepository = sseEmitterRepository;
    }

    // 조회 시작 (조회 수 증가)
    public void publishStartViewingEvent(Long storeId, Long userId) {

        // 가게에 접속
        sseEmitterRepository.linkUserToStore(storeId, userId);

        // 조회 수 증가 이벤트 발행
        redissonTopic.publish("start:" + storeId);
    }

    // 조회 종료 (조회 수 감소)
    public void publishStopViewingEvent(Long storeId, Long userId) {

        // 가게에서 접속 해제
        sseEmitterRepository.deleteUserInStore(storeId, userId);

        // 조회 수 감소 이벤트 발행
        redissonTopic.publish("stop:" + storeId);
    }

    // 현재 조회 수 반환
    public Long getCurrentViewCount(Long storeId) {
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
                String eventType = parts[0]; // start 또는 stop
                String storeId = parts[1];
                RAtomicLong viewCounter = redissonClient.getAtomicLong(PREFIX + storeId);
                // 만료시간 설정 - 10분
                viewCounter.expire(Duration.ofSeconds(EXPIRATION_TIME_SECONDS));

                if (eventType.equals("start")) {
                    viewCounter.incrementAndGet(); // 조회 수 증가
                    sseEmitterService.sendStoreViewNotification(Long.valueOf(storeId), viewCounter.get());
                } else if (eventType.equals("stop")) {
                    if(viewCounter.get() > 0) {
                        viewCounter.decrementAndGet(); // 조회 수 감소
                        sseEmitterService.sendStoreViewNotification(Long.valueOf(storeId), viewCounter.get());
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