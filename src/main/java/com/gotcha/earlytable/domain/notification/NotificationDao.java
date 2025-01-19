package com.gotcha.earlytable.domain.notification;


import com.gotcha.earlytable.domain.notification.entity.Notification;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class NotificationDao {

    private final RedissonClient redissonClient;

    public NotificationDao(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    // String 데이터 저장
    public void setValues(String key, String data) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(data);
    }

    public void setValues(String key, String data, Duration duration) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(data, Duration.ofDays(duration.toDays()));
    }

    public String getValues(String key) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    public void deleteValues(String key) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.delete();
    }

    // Notification 리스트 저장 및 관리
    public void setValuesForNotification(String key) {
        RList<Notification> list = redissonClient.getList(key);
        list.clear(); // 빈 리스트로 초기화
    }

    public List<Notification> getValuesForNotification(String key) {
        RList<Notification> list = redissonClient.getList(key);
        return list.readAll(); // 모든 데이터를 읽어옴
    }

    public void updateValuesForNotification(String key, Notification notification) {
        RList<Notification> list = redissonClient.getList(key);
        list.add(notification); // 새로운 Notification 추가
    }

    public void deleteValuesForNotification(String key) {
        RList<Notification> list = redissonClient.getList(key);
        list.clear(); // 리스트 내용 비우기
    }
}
