package com.gltqe.wladmin.commons.utils;

import com.gltqe.wladmin.commons.common.ConfigConstant;
import com.gltqe.wladmin.system.entity.po.SysConfig;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;

/**
 * @author gltqe
 * @date 2025/2/20 8:54
 */
public class ConfigUtil {

    private static final StringRedisTemplate REDIS = SpringContextUtil.getBean(StringRedisTemplate.class);

    public static void removeAllCache() {
        Set<String> keys = REDIS.keys(ConfigConstant.CONFIG_KEY + "*");
        if (keys != null && !keys.isEmpty()) {
            REDIS.delete(keys);
        }
    }

    public static void addCache(SysConfig sysConfig) {
        if (sysConfig != null) {
            addCache(sysConfig.getCode(), sysConfig.getValue());
        }
    }

    public static void addCache(String configCode, String configValue) {
        REDIS.opsForValue().set(ConfigConstant.CONFIG_KEY + configCode, configValue);
    }

    public static void removeCache(String configCode) {
        REDIS.delete(ConfigConstant.CONFIG_KEY + configCode);
    }

    public static String getCache(String configCode) {
        return REDIS.opsForValue().get(ConfigConstant.CONFIG_KEY + configCode);
    }
}
