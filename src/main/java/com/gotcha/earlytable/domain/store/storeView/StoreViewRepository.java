package com.gotcha.earlytable.domain.store.storeView;

import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;


@Repository
public class StoreViewRepository {

    private static final long EXPIRATION_TIME_SECONDS = 3600; // 만료 시간 (1시간)

    private final RMapCache<Long, Set<Long>> storeUserMap;

    public StoreViewRepository(RedissonClient redissonClient) {
        this.storeUserMap = redissonClient.getMapCache("store:user:map");
    }


    // 가게에 유저 연결
    @Transactional
    public boolean linkUserToStore(Long storeId, Long userId) {
        storeUserMap.putIfAbsent(storeId, new HashSet<>());

        if(storeUserMap.get(storeId).contains(userId)) {
            return true;
        }

        storeUserMap.computeIfPresent(storeId, (key, users) -> {
            users.add(userId);
            return users; // 변경된 Set을 다시 저장
        });

        // 만료 시간 갱신
        storeUserMap.expire(Duration.ofSeconds(EXPIRATION_TIME_SECONDS));

        return false;
    }

    // 가게에서 유저 연결 해제
    @Transactional
    public boolean deleteUserInStore(Long storeId, Long userId) {
        // 없으면 false
        if(!storeUserMap.get(storeId).contains(userId)) {
            return false;
        }

        storeUserMap.computeIfPresent(storeId, (key, users) -> {
            users.remove(userId);
            return users;
        });

        // 만료 시간 갱신
        storeUserMap.expire(Duration.ofSeconds(EXPIRATION_TIME_SECONDS));

        return true;
    }

    // 가게 연결된 유저 가져오기
    @Transactional(readOnly = true)
    public Set<Long> findUserIdsByStoreId(Long storeId) {
        return storeUserMap.getOrDefault(storeId, new HashSet<>());
    }

    @Transactional(readOnly = true)
    public Integer getCountOfUsersInStore(Long storeId) {
        return storeUserMap.getOrDefault(storeId, new HashSet<>()).size();
    }
}