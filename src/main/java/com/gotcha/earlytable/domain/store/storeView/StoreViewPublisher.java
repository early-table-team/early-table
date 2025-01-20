package com.gotcha.earlytable.domain.store.storeView;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;


@Service
public class StoreViewPublisher {

    private final RTopic redissonTopic;
    private final StoreViewRepository storeViewRepository;

    public StoreViewPublisher(RedissonClient redissonClient, StoreViewRepository storeViewRepository) {

        this.redissonTopic = redissonClient.getTopic("store:view:count");
        this.storeViewRepository = storeViewRepository;
    }

    // 조회 시작 (조회 수 증가 이벤트 발행)
    public void publishStartViewingEvent(Long storeId, Long userId) {
        boolean isConflict = storeViewRepository.linkUserToStore(storeId, userId);

        // 이미 중복 조회일 경우
        if(isConflict) {
            return;
        }

        redissonTopic.publish(storeId + ":" + Long.valueOf(storeViewRepository.getCountOfUsersInStore(storeId)));
    }

    // 조회 종료 (조회 수 감소 이벤트 발행)
    public void publishStopViewingEvent(Long storeId, Long userId) {
        boolean isSuccess = storeViewRepository.deleteUserInStore(storeId, userId);

        // 조회하지 않는 사용자를 제거하려고 할 경우
        if(!isSuccess) {
            return;
        }

        redissonTopic.publish(storeId + ":" + Long.valueOf(storeViewRepository.getCountOfUsersInStore(storeId)));
    }
}
