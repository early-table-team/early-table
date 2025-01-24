package com.gotcha.earlytable.domain.user;

import org.redisson.api.RedissonClient;
import org.redisson.api.RBucket;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RefreshTokenService {

    private final RedissonClient redissonClient;

    public RefreshTokenService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    // RefreshToken 저장
    public void saveRefreshToken(String username, String refreshToken) {
        RBucket<String> bucket = redissonClient.getBucket("refreshToken:" + username);
        bucket.set(refreshToken, Duration.ofDays(30));
    }

    // RefreshToken 가져오기
    public String getRefreshToken(String username) {
        RBucket<String> bucket = redissonClient.getBucket("refreshToken:" + username);
        return bucket.get();
    }

    // RefreshToken 삭제
    public void deleteRefreshToken(String username) {
        RBucket<String> bucket = redissonClient.getBucket("refreshToken:" + username);
        bucket.delete();
    }

    // RefreshToken 검증
    public boolean validateRefreshToken(String username, String refreshToken) {
        String storedToken = getRefreshToken(username);
        return storedToken != null && storedToken.equals(refreshToken);
    }
}
