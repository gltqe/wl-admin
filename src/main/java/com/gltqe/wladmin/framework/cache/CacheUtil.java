package com.gltqe.wladmin.framework.cache;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;

/**
 * 缓存工具类
 * @author gltqe
 * @date 2025/3/31 11:43
 */
public class CacheUtil {

    private static final CacheService cacheService = SpringUtil.getBean(CacheService.class);

    public static void set(String key, Object value) {
        cacheService.set(key, value);
    }

    public static void set(String key, Object value, long ttl) {
        cacheService.set(key, value, ttl);
    }

    public static Object get(String key) {
        return cacheService.get(key);
    }

    public static <T> T get(String key, Class<T> tClass) {
        Object o = get(key);
        if (o == null) {
            return null;
        } else {
            return JSONUtil.parseObj(o).toBean(tClass);
        }
    }
}
