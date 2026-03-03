package com.gltqe.wladmin.framework.cache;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSONObject;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 缓存工具类
 * @author gltqe
 * @date 2025/3/31 11:43
 */
public class CacheUtil {

    private static final CacheService cacheService = SpringUtil.getBean(CacheService.class);

    /**
     * 设置缓存键值对
     * @param key
     * @param value
     */
    public static void set(String key, Object value) {
        cacheService.set(key, value);
    }

    /**
     * 设置缓存键值对、缓存时间
     * @param key
     * @param value
     * @param ttl
     */
    public static void set(String key, Object value, long ttl) {
        cacheService.set(key, value, ttl);
    }

    /**
     * 获取缓存值
     * @param key
     */
    public static Object get(String key) {
        return cacheService.get(key);
    }

    /**
     * 获取缓存值转为对应类
     * @param key
     * @param tClass
     * @param <T>
     */
    public static <T> T get(String key, Class<T> tClass) {
        Object o = get(key);
        if (o == null) {
            return null;
        } else {
            return JSONObject.parseObject(JSONObject.toJSONString(o), tClass);
        }
    }

    /**
     * 获取剩余过期时间
     * @param key
     * @return
     */
    public static long getRemainExpire(String key) {
        return cacheService.getRemainExpire(key);
    }

    /**
     * 删除缓存
     * @param key
     */
    public static void delete(String key) {
        cacheService.delete(key);
    }

    /**
     * 获取所有符合条件的key（本地缓存只支持 * ）
     * @param key
     * @return
     */
    public static Set<String> keys(String key) {
        return cacheService.keys(key);
    }

    /**
     * 判断是否包含key
     * @param key
     * @return
     */
    public static boolean containsKey(String key) {
        return cacheService.containsKey(key);
    }

    /**
     * 是否包含
     * @param key
     * @param member
     * @return
     */
    public static boolean isMember(String key, String member) {
        return cacheService.isMember(key, member);
    }

    /**
     * 当key不存在时保存
     * @param key
     * @param value
     * @param ttl
     * @param timeUnit
     * @return
     */
    public boolean setIfAbsent(String key, Object value, long ttl, TimeUnit timeUnit) {
        return cacheService.setIfAbsent(key, value, ttl, timeUnit);
    }

    /**
     * hashmap put方法
     * @param key
     * @param hashKey
     * @param value
     */
    public void hashPut(String key, String hashKey, Object value) {
        cacheService.hashPut(key, hashKey, value);
    }
}
