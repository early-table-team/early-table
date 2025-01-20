package com.gotcha.earlytable.domain.store.storeView;

import com.gotcha.earlytable.domain.notification.SseEmitterService;
import jakarta.annotation.PostConstruct;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
public class StoreViewSubscriber {

    private final RedissonClient redissonClient;
    private final SseEmitterService sseEmitterService;


    public StoreViewSubscriber(RedissonClient redissonClient, SseEmitterService sseEmitterService) {
        this.redissonClient = redissonClient;
        this.sseEmitterService = sseEmitterService;
    }


    // 실시간 조회 수 변경 구독
    public void subscribeToViewEvents() {
        RTopic redissonTopic = redissonClient.getTopic("store:view:count");

        // 리스너 연결
        redissonTopic.addListener(String.class, (channel, msg) -> {
            String[] parts = msg.split(":");
            if (parts.length == 2) {
                Long storeId = Long.valueOf(parts[0]);
                Long viewCount = Long.valueOf(parts[1]);

                // SSE 알림 전송
                sseEmitterService.sendStoreViewNotification(storeId, viewCount);
            }
        });
    }

    @PostConstruct
    public void init() {
        subscribeToViewEvents();
    }
}
