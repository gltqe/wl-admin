package com.gltqe.wladmin.framework.cache;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.script.RedisScript;

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
     * 批量删除
     * @param keys
     */
    default void delete(Collection<String> keys) {
        throw new RuntimeException("未实现[delete]方法");
    }

    /**
     * 获取所有符合条件的key（本地缓存支持 * ? 等通配符）
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

    /**
     * hashmap put方法
     * @param key
     * @param hashKey
     * @param value
     * @return
     */
    default void hashPut(String key, String hashKey, Object value) {
        throw new RuntimeException("未实现[hashPut]方法");
    }

    /**
     * hash get方法
     * @param key
     * @param hashKey
     * @return
     */
    default Object hashGet(String key, String hashKey) {
        throw new RuntimeException("未实现[hashGet]方法");
    }

    /**
     * 自增 每次加1
     * @param key
     * @return 自增后的值
     */
    default Long increment(String key) {
        throw new RuntimeException("未实现[increment]方法");
    }

    /**
     * 自增
     * @param key
     * @param delta 增量
     * @return 自增后的值
     */
    default Long increment(String key, long delta) {
        throw new RuntimeException("未实现[increment]方法");
    }

    /**
     * 执行redis脚本
     * @param script 脚本
     * @param keys 键列表
     * @param args 参数
     * @param <T> 返回类型
     * @return
     */
    default <T> T execute(RedisScript<T> script, List<String> keys, Object... args) {
        throw new RuntimeException("未实现[execute]方法");
    }

    /**
     * set add方法
     * @param key
     * @param values
     */
    default void setAdd(String key, Object... values) {
        throw new RuntimeException("未实现[setAdd]方法");
    }

    /**
     * set remove方法
     * @param key
     * @param values
     */
    default void setRemove(String key, Object... values) {
        throw new RuntimeException("未实现[setRemove]方法");
    }


}
