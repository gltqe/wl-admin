package com.gltqe.wladmin.framework.cache.local;

import cn.hutool.extra.spring.SpringUtil;
import com.gltqe.wladmin.commons.exception.WlException;
import com.gltqe.wladmin.commons.utils.SpringContextUtil;
import com.gltqe.wladmin.framework.cache.CacheProperties;
import com.gltqe.wladmin.framework.cache.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author gltqe
 * @date 2025/3/20 15:20
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "cache", value = "type", havingValue = "local")
public class LocalCacheServiceImpl implements CacheService {

    private static CacheProperties cacheProperties = SpringContextUtil.getBean(CacheProperties.class);

    private static final TimeCache<String, Object> TIMED_CACHE = new TimeCache<String, Object>(Integer.MAX_VALUE, cacheProperties.getLocalDefaultTtl(), cacheProperties.getLocalPeriod());

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
     * 设置缓存键值对、缓存时间、时间单位
     * @param key
     * @param value
     * @param ttl
     * @param timeUnit
     */
    @Override
    public void set(String key, Object value, long ttl, TimeUnit timeUnit) {
        TIMED_CACHE.put(key, value, timeUnit.toSeconds(ttl));
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
     * 获取缓存值并自动延期 (初始设置的过期时间)
     * @param key
     */
    @Override
    public Object get(String key, boolean autoExt) {
        return TIMED_CACHE.get(key, autoExt);
    }

    /**
     * 获取缓存值并自动延期 (自定义延期时间) (redis实现)
     * @param key
     * @param ttl
     * @param timeUnit
     * @return
     */
    @Override
    public Object get(String key, long ttl, TimeUnit timeUnit) {
        throw new RuntimeException("[local]未实现此方法,请尝试其他类型缓存");
    }

    /**
     * 获取剩余过期时间
     * @param key
     * @return
     */
    @Override
    public long getRemainExpire(String key) {
        return TIMED_CACHE.getRemainExpire(key);
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
        return TIMED_CACHE.isMember(key, member);
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
        if (TIMED_CACHE.containsKey(key)) {
            return false;
        } else {
            // 转换秒
            long seconds = timeUnit.toSeconds(ttl);
            TIMED_CACHE.put(key, value, seconds);
            return true;
        }
    }

    @Override
    public void hashPut(String key, String hashKey, Object value) {
        if (TIMED_CACHE.containsKey(key)) {
            Object o = TIMED_CACHE.get(key);
            if (o instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> hashMap = (Map<String, Object>) o;
                hashMap.put(hashKey, value);
            } else {
                throw new WlException("缓存类型异常，请检查后重试");
            }
        } else {
            Map<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put(hashKey, value);
            TIMED_CACHE.put(key, hashMap);
        }
    }
}
