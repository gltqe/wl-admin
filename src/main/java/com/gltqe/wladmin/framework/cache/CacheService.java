package com.gltqe.wladmin.framework.cache;

import java.util.Set;

/**
 * @author gltqe
 * @date 2025/3/20 15:16
 */
public interface CacheService {

    default void set(String key, Object value) {
        throw new RuntimeException("未实现[set]方法");
    }

    default void set(String key, Object value, long ttl) {
        throw new RuntimeException("未实现[set]方法");
    }

    default Object get(String key) {
        throw new RuntimeException("未实现[get]方法");
    }

    default void delete(String key) {
        throw new RuntimeException("未实现[delete]方法");
    }

    default boolean containsKey(String key) {
        throw new RuntimeException("未实现[containsKey]方法");
    }

    default Set<String> keys() {
        throw new RuntimeException("未实现[keys]方法");
    }
}
