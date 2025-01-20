package com.gotcha.earlytable.domain.notification;

import jakarta.annotation.PostConstruct;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Optional;

@Service
public class NotificationSubscriber {

    private final SseEmitterRepository sseEmitterRepository;
    private final RedissonClient redissonClient;

    public NotificationSubscriber(SseEmitterRepository sseEmitterRepository, RedissonClient redissonClient) {
        this.sseEmitterRepository = sseEmitterRepository;
        this.redissonClient = redissonClient;
    }

    private void subscribeToNotifications() {
        RTopic topic = redissonClient.getTopic("notification:send");
        topic.addListener(NotificationMessage.class, (channel, message) -> handleNotification(message));
    }

    private void handleNotification(NotificationMessage message) {
        Optional<SseEmitter> sseEmitter = sseEmitterRepository.findById(message.getUserId());
        if (sseEmitter.isEmpty()) {
            return;
        }

        try {
            sseEmitter.get().send(
                    SseEmitter.event()
                            .id(message.getUserId().toString())
                            .name(message.getNotificationType().name())
                            .data(message.getData())
            );
        } catch (IOException ex) {
            sseEmitterRepository.deleteById(message.getUserId());
            throw new RuntimeException("연결 오류 발생");
        }
    }

    @PostConstruct
    public void init() {
        subscribeToNotifications();
    }
}