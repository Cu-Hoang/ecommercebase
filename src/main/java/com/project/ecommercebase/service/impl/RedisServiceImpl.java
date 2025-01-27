package com.project.ecommercebase.service.impl;

import java.time.Duration;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import com.project.ecommercebase.enums.ErrorCode;
import com.project.ecommercebase.exception.AppException;
import com.project.ecommercebase.service.RedisService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    RedisTemplate<String, String> redisTemplate;

    @Override
    public void setKeyWithTTL(String key, String value, long timeoutInSeconds) {
        try {
            redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(timeoutInSeconds));
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(ErrorCode.CANNOT_SENDING_CODE);
        }
    }

    @Override
    public String getValue(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void delete(String key) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) redisTemplate.delete(key);
    }

    @Override
    public void deleteByPattern(String pattern) {
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();

        try (Cursor<byte[]> cursor = connection.scan(
                ScanOptions.scanOptions().match(pattern).count(1000).build())) {
            while (cursor.hasNext()) {
                byte[] key = cursor.next();
                redisTemplate.delete(new String(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
