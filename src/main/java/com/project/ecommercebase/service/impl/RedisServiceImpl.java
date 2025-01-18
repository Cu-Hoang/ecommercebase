package com.project.ecommercebase.service.impl;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
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
}
