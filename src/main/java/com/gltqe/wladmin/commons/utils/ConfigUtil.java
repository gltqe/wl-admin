package com.gltqe.wladmin.commons.utils;

import com.gltqe.wladmin.commons.common.ConfigConstant;
import com.gltqe.wladmin.framework.cache.CacheUtil;
import com.gltqe.wladmin.system.entity.po.SysConfig;

import java.util.Set;

/**
 * @author gltqe
 * @date 2025/2/20 8:54
 */
public class ConfigUtil {

    public static void removeAllCache() {
        Set<String> keys = CacheUtil.keys(ConfigConstant.CONFIG_KEY + "*");
        if (keys != null && !keys.isEmpty()) {
            CacheUtil.delete(keys);
        }
    }

    public static void addCache(SysConfig sysConfig) {
        if (sysConfig != null) {
            addCache(sysConfig.getCode(), sysConfig.getValue());
        }
    }

    public static void addCache(String configCode, String configValue) {
        CacheUtil.set(ConfigConstant.CONFIG_KEY + configCode, configValue);
    }

    public static void removeCache(String configCode) {
        CacheUtil.delete(ConfigConstant.CONFIG_KEY + configCode);
    }

    public static String getCache(String configCode) {
        return CacheUtil.get(ConfigConstant.CONFIG_KEY + configCode, String.class);
    }
}
