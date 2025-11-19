package com.gltqe.wladmin.framework.cache;

import java.util.Set;

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
     * 获取缓存值
     * @param key
     */
    default Object get(String key) {
        throw new RuntimeException("未实现[get]方法");
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
}
