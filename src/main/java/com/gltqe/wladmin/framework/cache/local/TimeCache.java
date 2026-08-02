package com.gltqe.wladmin.framework.cache.local;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author gltqe
 * @date 2025/3/27 10:29
 */
@Slf4j
public class TimeCache<K, V> {

    /**
     * 永不过期
     */
    private static final long NO_EXPIRE = -1L;

    private final Map<K, CacheObj<K, V>> MAP = new ConcurrentHashMap<>();

    /**
     * 容量 -1(0) 表示不限量
     */
    private final int capacity;

    /**
     * 默认过期时长 -1(0) 表示永不过期
     */
    private final long defaultTtl;

    /**
     * 初始化
     * @param capacity 容量
     * @param defaultTtl 默认过期时间
     * @param period 过期检测周期
     */
    public TimeCache(int capacity, long defaultTtl, long period) {
        this.capacity = capacity;
        this.defaultTtl = defaultTtl;
        if (period > 0) {
            ScheduledFuture<?> schedule = GlobalCleanTimer.getInstance().schedule(this::cleanExpire, period);
        }
    }

    /**
     * 清理过期key
     * @author gltqe
     * @date 2025/3/28 13:32
     * return
     */
    private void cleanExpire() {
        for (Map.Entry<K, CacheObj<K, V>> cacheObjEntry : MAP.entrySet()) {
            try {
                CacheObj<K, V> value = cacheObjEntry.getValue();
                if (value != null && value.isExpired()) {
                    V remove = remove(cacheObjEntry.getKey());
                }
            } catch (Exception e) {
                log.info("清理过期缓存异常", e);
                continue;
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
            // 2参remove: 仅当仍是这个过期对象时才删,避免并发下误删刚写入的新值
            MAP.remove(key, kvCacheObj);
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
     * 当key不存在或已过期时保存,返回是否保存成功 (原子操作)
     * @param key
     * @param value
     * @param ttl
     * @return
     */
    public boolean setIfAbsent(K key, V value, long ttl) {
        CacheObj<K, V> newObj = new CacheObj<>(key, value, ttl);
        CacheObj<K, V> result = MAP.compute(key, (k, existing) -> {
            // 不存在或已过期则写入
            if (existing == null || existing.isExpired()) {
                if (capacity > 0 && MAP.size() >= capacity) {
                    throw new RuntimeException("超出最大限制:" + capacity);
                }
                return newObj;
            }
            return existing;
        });
        return result == newObj;
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
                return TimeUnit.MILLISECONDS.toSeconds(lastGet + cacheObj.getTtl() - System.currentTimeMillis());
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
     * 自增 返回自增后的值
     * key不存在或已过期时,重新创建且永不过期(与redis的incr一致)
     *
     * @param key
     * @param delta
     * @return
     */
    @SuppressWarnings("unchecked")
    public long increment(K key, long delta) {
        CacheObj<K, V> cacheObj = MAP.compute(key, (k, obj) -> {
            // 已存在且未过期,直接累加
            if (obj != null && !obj.isExpired()) {
                long newVal;
                Object value = obj.getValue();
                // 只支持整数,浮点数与其他类型与redis一致直接报错
                if (value instanceof Integer || value instanceof Long || value instanceof Short
                        || value instanceof Byte || value instanceof BigInteger) {
                    newVal = ((Number) value).longValue() + delta;
                } else if (value instanceof String) {
                    try {
                        newVal = Long.parseLong((String) value) + delta;
                    } catch (NumberFormatException e) {
                        throw new RuntimeException("缓存值不是整数,无法自增:" + k);
                    }
                } else {
                    throw new RuntimeException("缓存值不是整数,无法自增:" + k);
                }
                obj.setValue((V) Long.valueOf(newVal));
                return obj;
            }
            // 不存在或已过期,重新创建 (永不过期)
            if (capacity > 0 && MAP.size() >= capacity) {
                throw new RuntimeException("超出最大限制:" + capacity);
            }
            return new CacheObj<>(k, (V) Long.valueOf(delta), NO_EXPIRE);
        });
        return ((Number) cacheObj.getValue()).longValue();
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
        CacheObj<K, V> obj = MAP.get(key);
        if (obj == null) {
            return false;
        }
        // 过期即视为不存在,并顺手删除(与getCacheObj一致)
        if (obj.isExpired()) {
            // 2参remove: 仅当仍是这个过期对象时才删,避免并发下误删刚写入的新值
            MAP.remove(key, obj);
            return false;
        }
        return true;
    }

    /**
     * 获取所有符合条件的key (支持redis glob通配符: * 任意 ? 单个 [a-z]字符类 \转义)
     * @param pattern
     * @return
     */
    public Set<K> keys(String pattern) {
        String regex = "*".equals(pattern) ? null : globToRegex(pattern);
        Set<K> keys = new HashSet<>();
        for (Map.Entry<K, CacheObj<K, V>> e : MAP.entrySet()) {
            // 过期即跳过并顺手删除,与redis keys一致
            if (e.getValue() == null || e.getValue().isExpired()) {
                // 2参remove: 仅当仍是这个过期对象时才删,避免并发下误删刚写入的新值
                MAP.remove(e.getKey(), e.getValue());
                continue;
            }
            if (regex == null || e.getKey().toString().matches(regex)) {
                keys.add(e.getKey());
            }
        }
        return keys;
    }

    /**
     * redis glob通配符转正则
     * @param pattern
     * @return
     */
    private String globToRegex(String pattern) {
        StringBuilder sb = new StringBuilder();
        int length = pattern.length();
        for (int i = 0; i < length; i++) {
            char c = pattern.charAt(i);
            switch (c) {
                case '*':
                    // 任意字符
                    sb.append(".*");
                    break;
                case '?':
                    // 单个任意字符
                    sb.append('.');
                    break;
                case '\\':
                    // 转义下一个字符,当字面量
                    if (i + 1 < length) {
                        sb.append(Pattern.quote(String.valueOf(pattern.charAt(++i))));
                    } else {
                        // 结尾单独的反斜杠,当字面量
                        sb.append("\\\\");
                    }
                    break;
                case '[': {
                    // 字符类原样复制
                    int close = pattern.indexOf(']', i + 1);
                    if (close > i + 1) {
                        sb.append(pattern, i, close + 1);
                        i = close;
                    } else if (close == i + 1) {
                        // 空字符类,永不匹配
                        sb.append("(?!)");
                        i = close;
                    } else {
                        // 未闭合的 [,当字面量
                        sb.append("\\[");
                    }
                    break;
                }
                default:
                    // 其它正则元字符转义为字面量
                    if (".+^$()|{}]".indexOf(c) >= 0) {
                        sb.append('\\').append(c);
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }

    /**
     * 是否包含
     * @param key
     * @param member
     * @return
     */
    public boolean isMember(K key, K member) {
        CacheObj<K, V> obj = MAP.get(key);
        if (obj == null) {
            return false;
        }
        // 过期即视为不存在,并顺手删除(与getCacheObj一致)
        if (obj.isExpired()) {
            // 2参remove: 仅当仍是这个过期对象时才删,避免并发下误删刚写入的新值
            MAP.remove(key, obj);
            return false;
        }
        V value = obj.getValue();
        if (value instanceof Set<?>) {
            return ((Set<?>) value).contains(member);
        } else {
            return false;
        }
    }
}
