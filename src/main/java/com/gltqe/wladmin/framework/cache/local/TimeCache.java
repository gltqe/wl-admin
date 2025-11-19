package com.gltqe.wladmin.framework.cache.local;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author gltqe
 * @date 2025/3/27 10:29
 */
public class TimeCache<K, V> {

    private final Map<K, CacheObj<K, V>> MAP = new ConcurrentHashMap<>();

    /**
     * 容量 -1(0) 表示不限量
     */
    private final int capacity;

    /**
     * 默认过期时长 -1(0) 表示永不过期
     */
    private final long defaultTtl;


    public TimeCache(int capacity, long defaultTtl, long period) {
        this.capacity = capacity;
        this.defaultTtl = defaultTtl;
        if (period > 0) {
            ScheduledFuture<?> schedule = GlobalCleanTimer.getInstance().schedule(this::cleanExpire, period);
        }
    }

    /**
     * @author gltqe
     * @date 2025/3/28 13:32
     * return
     */
    private void cleanExpire() {
        for (Map.Entry<K, CacheObj<K, V>> cacheObjEntry : MAP.entrySet()) {
            CacheObj<K, V> value = cacheObjEntry.getValue();
            if (value != null && value.isExpired()) {
                V remove = remove(cacheObjEntry.getKey());
            }
        }
    }

    /**
     * 获取缓存对象 // 不会延期 //只有获取值时会
     *
     * @param key
     * @author gltqe
     * @date 2025/3/27 18:39
     * @return: V
     */
    private CacheObj<K, V> getCacheObj(K key) {
        CacheObj<K, V> kvCacheObj = MAP.get(key);
        // 判断是否过期 过期删除
        if (kvCacheObj == null) {
            return null;
        }
        if (kvCacheObj.isExpired()) {
            MAP.remove(key);
            return null;
        }
        return kvCacheObj;
    }

    /**
     * @param key
     * @param value
     * @param ttl
     * @author gltqe
     * @date 2025/3/27 17:36
     */
    public void put(K key, V value, long ttl) {
        CacheObj<K, V> obj = new CacheObj<>(key, value, ttl);
        if (capacity > 0 && MAP.size() < capacity) {
            MAP.put(key, obj);
        } else {
            throw new RuntimeException("超出最大限制:" + capacity);
        }
    }

    /**
     * @param key
     * @param value
     * @author gltqe
     * @date 2025/3/27 17:36
     */
    public void put(K key, V value) {
        put(key, value, defaultTtl);
    }

    /**
     * 获取缓存值并自动延期
     * @param key
     * @param autoExt
     * @author gltqe
     * @date 2025/3/28 14:46
     * @return: V
     */
    public V get(K key, boolean autoExt) {
        CacheObj<K, V> cacheObj = getCacheObj(key);
        if (cacheObj != null) {
            return cacheObj.get(autoExt);
        }
        return null;
    }

    /**
     * 默认不延期
     *
     * @param key
     * @author gltqe
     * @date 2025/3/28 14:45
     * @return: V
     */
    public V get(K key) {
        return get(key, false);
    }

    /**
     * 获取剩余过期时长
     *
     * @param key
     * @author gltqe
     * @date 2025/3/27 18:39
     * @return: V
     */
    public long getRemainExpire(K key) {
        CacheObj<K, V> cacheObj = getCacheObj(key);
        if (cacheObj != null && !cacheObj.isExpired()) {
            if (cacheObj.getTtl() > 0) {
                long lastGet = cacheObj.getLastGet();
                return lastGet + cacheObj.getTtl() - System.currentTimeMillis();
            } else {
                return -1;
            }
        } else {
            return -2;
        }
    }

    /**
     * @param key
     * @author gltqe
     * @date 2025/3/28 13:40
     * @return: V
     */
    public V remove(K key) {
        CacheObj<K, V> cacheObj = MAP.remove(key);
        if (cacheObj != null) {
            return cacheObj.getValue();
        }
        return null;
    }

    /**
     * 包含key
     *
     * @param key
     * @author gltqe
     * @date 2025/3/31 13:53
     * @return: boolean
     */
    public boolean containsKey(K key) {
        return MAP.containsKey(key);
    }


    /**
     * 获取所有符合条件的key（本地缓存只支持 * ）
     * @param pattern
     * @return
     */
    public Set<K> keys(String pattern) {
        if ("*".equals(pattern)) {
            return MAP.keySet();
        }
        Set<K> keys = new HashSet<>();
        if (pattern.contains("*")) {
            String regex = pattern.replace("*", ".*");
            for (K key : MAP.keySet()) {
                if (key.toString().matches(regex)) {
                    keys.add(key);
                }
            }
        }
        return keys;
    }

    /**
     * 是否包含
     * @param key
     * @param member
     * @return
     */
    public boolean isMember(K key, K member) {
        CacheObj<K, V> kvCacheObj = MAP.get(key);
        V value = kvCacheObj.getValue();
        if (value instanceof Set<?>) {
            return ((Set<?>) value).contains(member);
        } else {
            return false;
        }
    }
}
