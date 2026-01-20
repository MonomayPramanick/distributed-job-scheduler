package com.backend.service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisLockService {

    private final StringRedisTemplate redisTemplate;

    public RedisLockService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean acquire(String key, int ttlSeconds) {
        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(key, "locked", ttlSeconds, TimeUnit.SECONDS);

        return Boolean.TRUE.equals(success);
    }

    public void release(String key) {
        redisTemplate.delete(key);
    }
}
