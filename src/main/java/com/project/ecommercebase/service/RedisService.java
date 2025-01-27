package com.project.ecommercebase.service;

public interface RedisService {
    void setKeyWithTTL(String key, String value, long timeoutInSeconds);

    String getValue(String key);

    void delete(String key);

    void deleteByPattern(String pattern);
}
