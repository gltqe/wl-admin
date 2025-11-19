package com.gltqe.wladmin.framework.cache.local;

import com.gltqe.wladmin.framework.cache.CacheService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * @author gltqe
 * @date 2025/3/20 15:20
 */
@Configuration
@ConditionalOnProperty(prefix = "cache", value = "type", havingValue = "local")
public class LocalCacheServiceImpl implements CacheService {

    private static final TimeCache<String, Object> TIMED_CACHE = new TimeCache<String, Object>(Integer.MAX_VALUE, -1, 3);

    /**
     * 设置缓存键值对
     * @param key
     * @param value
     */
    @Override
    public void set(String key, Object value) {
        TIMED_CACHE.put(key, value);
    }

    /**
     * 设置缓存键值对、缓存时间
     * @param key
     * @param value
     * @param ttl
     */
    @Override
    public void set(String key, Object value, long ttl) {
        TIMED_CACHE.put(key, value, ttl);
    }

    /**
     * 获取缓存值
     * @param key
     */
    @Override
    public Object get(String key) {
        return TIMED_CACHE.get(key);
    }

    /**
     * 获取缓存值并自动延期
     * @param key
     */
    public Object get(String key, boolean autoExt) {
        return TIMED_CACHE.get(key, autoExt);
    }

    /**
     * 删除缓存
     * @param key
     */
    @Override
    public void delete(String key) {
        CacheService.super.delete(key);
    }

    /**
     * 获取所有符合条件的key（本地缓存只支持 * ）
     * @param key
     * @return
     */
    @Override
    public Set<String> keys(String key) {
        return TIMED_CACHE.keys(key);
    }

    public long getRemainExpire(String key) {
        return TIMED_CACHE.getRemainExpire(key);
    }

    /**
     * 判断是否包含key
     * @param key
     * @return
     */
    @Override
    public boolean containsKey(String key) {
        return TIMED_CACHE.containsKey(key);
    }

    /**
     * 是否包含
     * @param key
     * @param member
     * @return
     */
    @Override
    public boolean isMember(String key, String member) {
        return TIMED_CACHE.isMember(key,member);
    }
}
