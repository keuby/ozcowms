package com.keuby.ozcowms.gateway.utils;

import com.alibaba.fastjson.JSON;
import com.keuby.ozcowms.common.utils.JsonUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class RedisClient {

    private final StringRedisTemplate redisTemplate;

    public RedisClient(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean set(String key, String value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean delete(String key) {
        try {
            if (StringUtils.isEmpty(key)) {
                return true;
            }
            return redisTemplate.delete(key);
        } catch (Exception e) {
            return false;
        }
    }

    public <T> boolean set(String key, T value) {
        String strVal = JsonUtils.toJson(value);
        return set(key, strVal);
    }

    public <T> T get(String key, Class<T> tClass) {
        String value = get(key);
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return JsonUtils.parse(value, tClass);
    }
}
