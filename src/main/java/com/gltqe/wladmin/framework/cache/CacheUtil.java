package com.gltqe.wladmin.framework.cache;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;

import java.util.Set;

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
            return JSONUtil.parseObj(o).toBean(tClass);
        }
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
        return cacheService.isMember(key,member);
    }
}
