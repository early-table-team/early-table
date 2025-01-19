package com.gotcha.earlytable.domain.notification;

import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SseEmitterRepository {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<Long, List<Long>> storeUserMap = new ConcurrentHashMap<>(); // storeId -> userIds

    // emitter 찾기
    public SseEmitter findByIdOrElseThrow(Long userId) {
        return Optional.ofNullable(emitters.get(userId)).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }


    public Optional<SseEmitter> findById(Long userId) {
        return Optional.ofNullable(emitters.get(userId));
    }

    public boolean existsById(Long userId) {
        return emitters.containsKey(userId);
    }

    public SseEmitter save(Long userId, SseEmitter sseEmitter) {
        emitters.put(userId, sseEmitter);
        return emitters.get(userId);
    }

    public void deleteById(Long userId) {
        emitters.remove(userId);
    }

    // 가게에 유저 연결
    public void linkUserToStore(Long storeId, Long userId) {
        storeUserMap.computeIfAbsent(storeId, k -> new ArrayList<>()).add(userId);
    }

    // 가게 연결된 유저 가져오기
    public List<Long> findUserIdsByStoreId(Long storeId) {
        return storeUserMap.getOrDefault(storeId, new ArrayList<>());
    }

    // 가게에서 연결 해제
    public void deleteUserInStore(Long storeId, Long userId) {
        storeUserMap.computeIfAbsent(storeId, k -> new ArrayList<>()).remove(userId);
    }
}