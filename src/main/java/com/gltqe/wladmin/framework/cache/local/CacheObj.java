package com.gltqe.wladmin.framework.cache.local;

import lombok.Data;

/**
 * @author gltqe
 * @date 2025/3/27 15:36
 */
@Data
public class CacheObj<K, V> {

    /**
     * 缓存key
     */
    private K key;

    /**
     * 缓存value
     */
    private V value;

    /**
     * 过期时长 -1 表示永不过期
     */
    private long ttl;

    /**
     * 上次访问时间
     */
    private long lastGet;


    public CacheObj(K key, V value, long ttl) {
        this.key = key;
        this.value = value;
        this.ttl = ttl;
        this.lastGet = System.currentTimeMillis();
    }

    public boolean isExpired() {
        if (this.ttl > 0) {
            return (System.currentTimeMillis() - this.lastGet) > this.ttl;
        }
        return false;
    }

    public V get(boolean autoExt){
        if (autoExt){
            this.lastGet = System.currentTimeMillis();
        }
        return this.value;
    }

    public V get(){
        return get(false);
    }
}
