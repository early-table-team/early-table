package com.gotcha.earlytable.domain.notification;

import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SseEmitterRepository {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitterRepository() {
    }


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
}