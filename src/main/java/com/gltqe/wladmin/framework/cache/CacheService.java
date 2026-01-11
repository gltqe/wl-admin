package com.gltqe.wladmin.framework.cache;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author gltqe
 * @date 2025/3/20 15:16
 */
public interface CacheService {

    /**
     * 设置缓存键值对
     * @param key
     * @param value
     */
    default void set(String key, Object value) {
        throw new RuntimeException("未实现[set]方法");
    }

    /**
     * 设置缓存键值对、缓存时间
     * @param key
     * @param value
     * @param ttl
     */
    default void set(String key, Object value, long ttl) {
        throw new RuntimeException("未实现[set]方法");
    }

    /**
     * 设置缓存键值对、缓存时间、时间单位
     * @param key
     * @param value
     * @param ttl
     * @param timeUnit
     */
    default void set(String key, Object value, long ttl, TimeUnit timeUnit) {
        throw new RuntimeException("未实现[set]方法");
    }

    /**
     * 获取缓存值
     * @param key
     */
    default Object get(String key) {
        throw new RuntimeException("未实现[get]方法");
    }

    /**
     * 获取缓存值并自动延期 (初始设置的过期时间) (local实现)
     * @param key
     * @param autoExt
     * @return
     */
    default Object get(String key, boolean autoExt) {
        throw new RuntimeException("未实现[get]方法");
    }

    /**
     * 获取缓存值并自动延期 (自定义延期时间) (redis实现)
     * @param key
     * @param ttl
     * @param timeUnit
     * @return
     */
    default Object get(String key, long ttl, TimeUnit timeUnit) {
        throw new RuntimeException("未实现[get]方法");
    }

    /**
     * 获取剩余过期时间
     * @param key
     * @return
     */
    default long getRemainExpire(String key) {
        throw new RuntimeException("未实现[getRemainExpire]方法");
    }

    /**
     * 删除缓存
     * @param key
     */
    default void delete(String key) {
        throw new RuntimeException("未实现[delete]方法");
    }

    /**
     * 获取所有符合条件的key（本地缓存只支持 * ）
     * @param key
     * @return
     */
    default Set<String> keys(String key) {
        throw new RuntimeException("未实现[keys]方法");
    }

    /**
     * 判断是否包含key
     * @param key
     * @return
     */
    default boolean containsKey(String key) {
        throw new RuntimeException("未实现[containsKey]方法");
    }

    /**
     * 是否包含
     * @param key
     * @param member
     * @return
     */
    default boolean isMember(String key, String member) {
        throw new RuntimeException("未实现[isMember]方法");
    }

    /**
     * 当key不存在时保存
     * @param key
     * @param value
     * @param ttl
     * @param timeUnit
     * @return
     */
    default boolean setIfAbsent(String key, Object value, long ttl, TimeUnit timeUnit) {
        throw new RuntimeException("未实现[setIfAbsent]方法");
    }
}
