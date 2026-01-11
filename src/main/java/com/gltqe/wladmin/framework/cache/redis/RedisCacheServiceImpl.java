package com.gltqe.wladmin.framework.cache.redis;

import cn.hutool.json.JSONUtil;
import com.gltqe.wladmin.framework.cache.CacheService;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author gltqe
 * @date 2025/3/21 17:29
 */
@Configuration
@ConditionalOnProperty(prefix = "cache", value = "type", havingValue = "redis")
public class RedisCacheServiceImpl implements CacheService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置缓存键值对
     * @param key
     * @param value
     */
    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value.toString());
    }

    /**
     * 设置缓存键值对、缓存时间
     * @param key
     * @param value
     * @param ttl
     */
    @Override
    public void set(String key, Object value, long ttl) {
        redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), ttl);
    }

    /**
     * 设置缓存键值对、缓存时间、时间单位
     * @param key
     * @param value
     * @param ttl
     * @param timeUnit
     */
    @Override
    public void set(String key, Object value, long ttl, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, ttl, timeUnit);
    }

    /**
     * 获取缓存值
     * @param key
     */
    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取缓存值并自动延期
     * @param key
     */
    @Override
    public Object get(String key, long ttl, TimeUnit timeUnit) {
        Object object = get(key);
        if (object != null) {
            redisTemplate.expire(key, ttl, timeUnit);
        }
        return object;
    }

    /**
     * 获取剩余过期时间
     * @param key
     * @return
     */
    @Override
    public long getRemainExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 删除缓存
     * @param key
     */
    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 获取所有符合条件的key（本地缓存只支持 * ）
     * @param key
     * @return
     */
    @Override
    public Set<String> keys(String key) {
        return redisTemplate.keys(key);
    }

    /**
     * 判断是否包含key
     * @param key
     * @return
     */
    @Override
    public boolean containsKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * set中是否包含
     * @param key
     * @param member
     * @return
     */
    @Override
    public boolean isMember(String key, String member) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, member));
    }

    /**
     * 当key不存在时保存
     * @param key
     * @param value
     * @param ttl
     * @param timeUnit
     * @return
     */
    @Override
    public boolean setIfAbsent(String key, Object value, long ttl, TimeUnit timeUnit) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, ttl, timeUnit);
    }
}
