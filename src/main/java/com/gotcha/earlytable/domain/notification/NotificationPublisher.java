package com.gotcha.earlytable.domain.notification;

import com.gotcha.earlytable.global.enums.NotificationType;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
public class NotificationPublisher {

    private final RedissonClient redissonClient;

    public NotificationPublisher(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public void publishNotification(Long userId, Object data, NotificationType notificationType) {
        String channel = "notification:send";
        RTopic topic = redissonClient.getTopic(channel);
        topic.publish(new NotificationMessage(userId, data, notificationType));
    }
}