package com.gltqe.wladmin.framework.cache.redis;

import com.gltqe.wladmin.framework.cache.CacheService;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author gltqe
 * @date 2025/3/21 17:29
 */
@Configuration
@ConditionalOnProperty(prefix = "cache",value = "type", havingValue = "redis")
public class RedisCacheServiceImpl implements CacheService {

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value.toString());
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

}
