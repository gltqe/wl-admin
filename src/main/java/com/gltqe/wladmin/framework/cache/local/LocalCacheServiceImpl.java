package com.gltqe.wladmin.framework.cache.local;

import com.gltqe.wladmin.framework.cache.CacheService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author gltqe
 * @date 2025/3/20 15:20
 */
@Configuration
@ConditionalOnProperty(prefix = "cache", value = "type", havingValue = "local")
public class LocalCacheServiceImpl implements CacheService {

    private static final TimeCache<String, Object> TIMED_CACHE = new TimeCache<String, Object>(Integer.MAX_VALUE, -1, 3);

    /**
     * 存入
     */
    @Override
    public void set(String key, Object value) {
        TIMED_CACHE.put(key, value);
    }

    /**
     * 存入-有效期
     */
    @Override
    public void set(String key, Object value, long ttl) {
        TIMED_CACHE.put(key, value, ttl);
    }

    /**
     * 获取
     */
    @Override
    public Object get(String key) {
        return TIMED_CACHE.get(key);
    }

    /**
     * 获取并延期
     */
    public Object get(String key, boolean autoExt) {
        return TIMED_CACHE.get(key, autoExt);
    }

    /**
     * 获取剩余存活时间
     */
    public long getRemainExpire(String key) {
        return TIMED_CACHE.getRemainExpire(key);
    }

    /**
     * 是否存在key
     */
    public boolean containsKey(String key) {
        return TIMED_CACHE.containsKey(key);
    }

}
