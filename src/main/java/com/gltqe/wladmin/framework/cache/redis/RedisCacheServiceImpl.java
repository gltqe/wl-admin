package com.gltqe.wladmin.framework.cache.redis;

import com.gltqe.wladmin.framework.cache.CacheService;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collection;
import java.util.List;
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
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置缓存键值对、缓存时间
     * @param key
     * @param value
     * @param ttl
     */
    @Override
    public void set(String key, Object value, long ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
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
     * 获取缓存值并自动延期 (初始设置的过期时间) (local实现)
     * @param key
     * @param autoExt
     * @return
     */
    @Override
    public Object get(String key, boolean autoExt) {
        throw new RuntimeException("[redis]未实现此方法,请尝试其他类型缓存");
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
     * 批量删除
     * @param keys
     */
    @Override
    public void delete(Collection<String> keys) {
        redisTemplate.delete(keys);
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

    /**
     * hashmap put方法
     * @param key
     * @param hashKey
     * @param value
     */
    @Override
    public void hashPut(String key, String hashKey, Object value) {
        redisTemplate.boundHashOps(key).put(hashKey, value);
    }

    /**
     * hash get方法
     * @param key
     * @param hashKey
     * @return
     */
    @Override
    public Object hashGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 自增 每次加1
     * @param key
     * @return
     */
    @Override
    public Long increment(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * 自增
     * @param key
     * @param delta
     * @return
     */
    @Override
    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 执行redis脚本
     * @param script
     * @param keys
     * @param args
     * @return
     */
    @Override
    public <T> T execute(RedisScript<T> script, List<String> keys, Object... args) {
        return redisTemplate.execute(script, keys, args);
    }

    /**
     * set add方法
     * @param key
     * @param values
     */
    @Override
    public void setAdd(String key, Object... values) {
        redisTemplate.opsForSet().add(key, values);
    }

    /**
     * set remove方法
     * @param key
     * @param values
     */
    @Override
    public void setRemove(String key, Object... values) {
        redisTemplate.opsForSet().remove(key, values);
    }


}
