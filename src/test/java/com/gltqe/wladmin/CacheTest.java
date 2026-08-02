package com.gltqe.wladmin;

import com.gltqe.wladmin.framework.cache.local.CacheObj;
import com.gltqe.wladmin.framework.cache.local.TimeCache;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TimeCache.keys glob通配符测试
 *
 * @author gltqe
 * @date 2026/8/2
 */
public class CacheTest {

    @Test
    public void testKeys() {
        // capacity不限, defaultTtl=-1永不过期, period=0不启动清理线程
        TimeCache<String, Object> cache = new TimeCache<>(Integer.MAX_VALUE, -1, 0);

        // ========== 先放入一些 key ==========
        cache.put("user:1", "a");
        cache.put("user:2", "b");
        cache.put("user:abc", "c");
        cache.put("id:5", "d");
        cache.put("id:55", "e");
        cache.put("id:a", "f");
        cache.put("a.b", "g");
        cache.put("a1b", "h");
        cache.put("a*b", "i");
        cache.put("aXb", "j");
        cache.put("a[", "k");
        cache.put("a[]b", "l");
        cache.put("user+1", "m");
        cache.put("user1", "n");

        // ========== 1. * 匹配全部 ==========
        Set<String> all = cache.keys("*");
        System.out.println("keys(*) = " + all);
        assertEquals(14, all.size());

        // ========== 2. 前缀 * ==========
        Set<String> userColon = cache.keys("user:*");
        System.out.println("keys(user:*) = " + userColon);
        assertEquals(3, userColon.size());
        assertTrue(userColon.contains("user:1"));
        assertTrue(userColon.contains("user:2"));
        assertTrue(userColon.contains("user:abc"));
        assertFalse(userColon.contains("user1")); // 没有冒号,不匹配

        // ========== 3. ? 匹配单个任意字符 ==========
        Set<String> userOne = cache.keys("user:?");
        System.out.println("keys(user:?) = " + userOne);
        assertEquals(2, userOne.size());
        assertTrue(userOne.contains("user:1"));
        assertTrue(userOne.contains("user:2"));
        assertFalse(userOne.contains("user:abc")); // 冒号后有3个字符,?只匹配1个

        // ========== 4. 字符类 [0-9] ==========
        Set<String> idOneDigit = cache.keys("id:[0-9]");
        System.out.println("keys(id:[0-9]) = " + idOneDigit);
        assertEquals(1, idOneDigit.size());
        assertTrue(idOneDigit.contains("id:5"));

        // ========== 5. 连续两个数字 [0-9][0-9] ==========
        Set<String> idTwoDigit = cache.keys("id:[0-9][0-9]");
        System.out.println("keys(id:[0-9][0-9]) = " + idTwoDigit);
        assertEquals(1, idTwoDigit.size());
        assertTrue(idTwoDigit.contains("id:55"));

        // ========== 6. 否定字符类 [^5] ==========
        Set<String> idNotFive = cache.keys("id:[^5]");
        System.out.println("keys(id:[^5]) = " + idNotFive);
        assertEquals(1, idNotFive.size());
        assertTrue(idNotFive.contains("id:a"));
        assertFalse(idNotFive.contains("id:5"));

        // ========== 7. 字面点 . 不当通配符 ==========
        Set<String> literalDot = cache.keys("a.b");
        System.out.println("keys(a.b) = " + literalDot);
        assertEquals(1, literalDot.size());
        assertTrue(literalDot.contains("a.b"));
        assertFalse(literalDot.contains("a1b"));

        // ========== 8. 转义 * : 匹配字面 * 而非通配 ==========
        Set<String> escapedStar = cache.keys("a\\*b");
        System.out.println("keys(a\\\\*b) = " + escapedStar);
        assertEquals(1, escapedStar.size());
        assertTrue(escapedStar.contains("a*b"));
        assertFalse(escapedStar.contains("aXb"));

        // ========== 9. 未闭合 [ 当字面量 ==========
        Set<String> unclosedBracket = cache.keys("a[");
        System.out.println("keys(a[) = " + unclosedBracket);
        assertEquals(1, unclosedBracket.size());
        assertTrue(unclosedBracket.contains("a["));

        // ========== 10. 空字符类 [] 永不匹配 ==========
        Set<String> emptyClass = cache.keys("a[]b");
        System.out.println("keys(a[]b) = " + emptyClass);
        assertTrue(emptyClass.isEmpty());

        // ========== 11. 正则元字符字面处理 ==========
        Set<String> plusKey = cache.keys("user+1");
        System.out.println("keys(user+1) = " + plusKey);
        assertEquals(1, plusKey.size());
        assertTrue(plusKey.contains("user+1"));
    }

    /**
     * 过期key不可见: containsKey/keys/isMember 都要过滤过期条目(对齐redis)
     */
    @Test
    public void testExpiredKeyInvisible() throws InterruptedException {
        TimeCache<String, Object> cache = new TimeCache<>(Integer.MAX_VALUE, -1, 0);
        cache.put("login:alice", "v1");       // 永不过期
        cache.put("login:bob", "v2", 1);      // 1秒后过期

        Set<Object> set = new HashSet<>();
        set.add("a");
        cache.put("login:set", set, 1);       // 1秒后过期,值是Set

        // 未过期: 三个方法都可见
        System.out.println("过期前 keys(login:*) = " + cache.keys("login:*"));
        assertTrue(cache.containsKey("login:alice"));
        assertTrue(cache.containsKey("login:bob"));
        assertEquals(3, cache.keys("login:*").size());
        assertTrue(cache.isMember("login:set", "a"));

        // 等待超过1秒让bob和set过期
        Thread.sleep(1200);

        System.out.println("过期后 keys(login:*) = " + cache.keys("login:*"));
        // 永不过期的alice还在
        assertTrue(cache.containsKey("login:alice"));
        assertTrue(cache.keys("login:*").contains("login:alice"));
        // 过期的bob不可见
        assertFalse(cache.containsKey("login:bob"));
        assertFalse(cache.keys("login:*").contains("login:bob"));
        assertEquals(1, cache.keys("login:*").size());
        // 过期的set不可见
        assertFalse(cache.isMember("login:set", "a"));
    }

    /**
     * getRemainExpire: 缺失-2 / 永不过期-1 / 有ttl返回剩余秒数且递减
     */
    @Test
    public void testGetRemainExpire() throws InterruptedException {
        TimeCache<String, Object> cache = new TimeCache<>(Integer.MAX_VALUE, -1, 0);
        // 缺失key
        assertEquals(-2L, cache.getRemainExpire("missing"));
        // 永不过期
        cache.put("never", "v");
        assertEquals(-1L, cache.getRemainExpire("never"));
        // 有ttl
        cache.put("ttl", "v", 10);   // 10秒
        long remain = cache.getRemainExpire("ttl");
        assertTrue(remain > 0 && remain <= 10, "剩余时间应在0~10秒,实际:" + remain);
        Thread.sleep(1100);
        long remain2 = cache.getRemainExpire("ttl");
        assertTrue(remain2 <= 9, "1.1秒后剩余应减少,实际:" + remain2);
    }

    /**
     * increment: 新key永不过期 / 各种数值类型 / 保留原TTL
     */
    @Test
    public void testIncrement() throws InterruptedException {
        TimeCache<String, Object> cache = new TimeCache<>(Integer.MAX_VALUE, -1, 0);

        // 新key自增 → 值=delta, 且永不过期
        assertEquals(5L, cache.increment("newKey", 5));
        assertEquals(5L, ((Number) cache.get("newKey")).longValue());
        assertEquals(-1L, cache.getRemainExpire("newKey"));

        // Integer值累加
        cache.put("intKey", 10);
        assertEquals(11L, cache.increment("intKey", 1));
        assertEquals(12L, cache.increment("intKey", 1));

        // Long值累加
        cache.put("longKey", 100L);
        assertEquals(101L, cache.increment("longKey", 1));

        // 数字字符串累加
        cache.put("strKey", "5");
        assertEquals(6L, cache.increment("strKey", 1));

        // 保留TTL: 带ttl的key自增后,剩余时间不重置(与redis incr一致)
        cache.put("ttlKey", 1, 60);   // 60秒
        Thread.sleep(1100);
        long before = cache.getRemainExpire("ttlKey");
        cache.increment("ttlKey", 1);
        long after = cache.getRemainExpire("ttlKey");
        assertTrue(after <= before, "自增不应重置过期时间, 前:" + before + " 后:" + after);
    }

    /**
     * increment: 浮点数/非数字字符串报错, 且map不变
     */
    @Test
    public void testIncrementError() {
        TimeCache<String, Object> cache = new TimeCache<>(Integer.MAX_VALUE, -1, 0);
        cache.put("doubleKey", 1.5);
        assertThrows(RuntimeException.class, () -> cache.increment("doubleKey", 1));
        cache.put("abcKey", "abc");
        assertThrows(RuntimeException.class, () -> cache.increment("abcKey", 1));
        // compute异常时map保持不变
        assertEquals(1.5, ((Number) cache.get("doubleKey")).doubleValue(), 0.001);
        assertEquals("abc", cache.get("abcKey"));
    }

    /**
     * setIfAbsent: 不存在写入成功 / 已存在不覆盖 / 过期视为不存在
     */
    @Test
    public void testSetIfAbsent() throws InterruptedException {
        TimeCache<String, Object> cache = new TimeCache<>(Integer.MAX_VALUE, -1, 0);
        // 不存在 → 成功
        assertTrue(cache.setIfAbsent("k1", "v1", -1));
        assertEquals("v1", cache.get("k1"));
        // 已存在 → 不覆盖
        assertFalse(cache.setIfAbsent("k1", "v2", -1));
        assertEquals("v1", cache.get("k1"));
        // 已过期 → 视为不存在,重新写入
        cache.put("k2", "old", 1);
        Thread.sleep(1200);
        assertTrue(cache.setIfAbsent("k2", "new", -1));
        assertEquals("new", cache.get("k2"));
    }

    /**
     * 容量限制: 超出抛异常
     */
    @Test
    public void testCapacityLimit() {
        TimeCache<String, Object> cache = new TimeCache<>(2, -1, 0);
        cache.put("a", 1);
        cache.put("b", 2);
        assertThrows(RuntimeException.class, () -> cache.put("c", 3));
        assertThrows(RuntimeException.class, () -> cache.setIfAbsent("c", 3, -1));
        // 删除一个后可以再放
        cache.remove("a");
        cache.put("c", 3);
        assertEquals(3, ((Number) cache.get("c")).intValue());
    }

    /**
     * get(key, true) 自动续期: 刷新后重新计时
     */
    @Test
    public void testAutoExt() throws InterruptedException {
        TimeCache<String, Object> cache = new TimeCache<>(Integer.MAX_VALUE, -1, 0);
        cache.put("key", "v", 1);   // 1秒过期

        Thread.sleep(500);
        cache.get("key", true);     // 刷新lastGet
        Thread.sleep(700);          // 距刷新0.7秒, 距初始put已1.2秒
        assertTrue(cache.containsKey("key"), "autoExt应续命,key不应过期");

        Thread.sleep(600);          // 距上次刷新1.3秒
        assertFalse(cache.containsKey("key"), "不刷新则应过期");
        // 缺失key get(true)返回null
        assertNull(cache.get("missing", true));
    }

    /**
     * get过期key: 返回null且从map移除
     */
    @Test
    public void testExpiredGetRemoves() throws InterruptedException {
        TimeCache<String, Object> cache = new TimeCache<>(Integer.MAX_VALUE, -1, 0);
        cache.put("key", "v", 1);
        Thread.sleep(1200);
        assertNull(cache.get("key"));
        assertFalse(cache.containsKey("key"));
        assertTrue(cache.keys("*").isEmpty());
    }

    /**
     * remove: 返回原值, 移除后keys不含该key
     */
    @Test
    public void testRemoveAndKeys() {
        TimeCache<String, Object> cache = new TimeCache<>(Integer.MAX_VALUE, -1, 0);
        cache.put("a", "A");
        cache.put("b", "B");
        assertEquals("A", cache.remove("a"));
        assertNull(cache.remove("missing"));
        assertFalse(cache.keys("*").contains("a"));
        assertTrue(cache.keys("*").contains("b"));
    }

    /**
     * 并发increment原子性: 10线程各自增1000次, 结果必须精确
     */
    @Test
    public void testConcurrentIncrement() throws InterruptedException {
        TimeCache<String, Object> cache = new TimeCache<>(Integer.MAX_VALUE, -1, 0);
        cache.put("counter", 0L);
        int threads = 10;
        int perThread = 1000;
        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            Thread t = new Thread(() -> {
                for (int j = 0; j < perThread; j++) {
                    cache.increment("counter", 1);
                }
            });
            list.add(t);
            t.start();
        }
        for (Thread t : list) {
            t.join();
        }
        assertEquals((long) threads * perThread, ((Number) cache.get("counter")).longValue());
    }

    /**
     * 并发setIfAbsent原子性: 10线程抢同一个key, 只能有一个成功
     */
    @Test
    public void testConcurrentSetIfAbsent() throws InterruptedException {
        TimeCache<String, Object> cache = new TimeCache<>(Integer.MAX_VALUE, -1, 0);
        AtomicInteger success = new AtomicInteger();
        int threads = 10;
        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            Thread t = new Thread(() -> {
                if (cache.setIfAbsent("once", "v", -1)) {
                    success.incrementAndGet();
                }
            });
            list.add(t);
            t.start();
        }
        for (Thread t : list) {
            t.join();
        }
        assertEquals(1, success.get());
        assertEquals("v", cache.get("once"));
    }

    /**
     * CacheObj构造器: ttl<=0永不过期 / ttl>0按毫秒存储 / autoExt刷新
     */
    @Test
    public void testCacheObj() throws InterruptedException {
        // ttl<=0 → 永不过期
        CacheObj<String, String> never = new CacheObj<>("k", "v", -1);
        assertEquals(-1L, never.getTtl());
        Thread.sleep(1100);
        assertFalse(never.isExpired());

        // ttl>0 → 秒转毫秒存储, 到期过期
        CacheObj<String, String> expire = new CacheObj<>("k", "v", 1);
        assertEquals(1000L, expire.getTtl());
        Thread.sleep(1100);
        assertTrue(expire.isExpired());

        // autoExt: 刷新lastGet重新计时
        CacheObj<String, String> ext = new CacheObj<>("k", "v", 1);
        Thread.sleep(600);
        ext.get(true);              // 刷新
        assertFalse(ext.isExpired());
        Thread.sleep(600);          // 距刷新0.6秒
        assertFalse(ext.isExpired());
        Thread.sleep(600);          // 距刷新1.2秒
        assertTrue(ext.isExpired());
    }
}
