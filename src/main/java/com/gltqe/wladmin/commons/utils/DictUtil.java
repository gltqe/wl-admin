package com.gltqe.wladmin.commons.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.gltqe.wladmin.commons.common.DictConstant;
import com.gltqe.wladmin.framework.excel.Dict;
import com.gltqe.wladmin.system.entity.po.SysDictItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author gltqe
 * @date 2025/2/20 8:54
 */
public class DictUtil {

    private static final RedisTemplate<String, String> redisTemplate = SpringContextUtil.getBean(RedisTemplate.class);

    /**
     * 默认map是否缓存
     */
    private static final boolean DEFAULT_CACHE = true;

    /**
     * 默认map缓存时间
     */
    private static final int DEFAULT_TIMEOUT = 5;


    /**
     * 默认键值对 code-item
     */
    private static final boolean DEFAULT_REVERSE = false;

    public static String getDictText(String dictCode, Object value) {
        return getDictText(dictCode, value, DEFAULT_CACHE);
    }

    public static String getDictText(String dictCode, Object value, boolean cache) {
        return getDictText(dictCode, value, cache, DEFAULT_TIMEOUT);
    }

    public static String getDictText(String dictCode, Object value, boolean cache, int timeout) {
        SysDictItem dictItem = getCacheDictItem(dictCode, value, cache, timeout);
        return dictItem == null ? null : dictItem.getText();
    }

    public static String getDictText(Dict dict, Object value) {
        return getDictText(dict, value, DEFAULT_CACHE);
    }

    public static String getDictText(Dict dict, Object value, boolean cache) {
        return getDictText(dict, value, cache, DEFAULT_TIMEOUT);
    }

    public static String getDictText(Dict dict, Object value, boolean cache, int timeout) {
        if (dict != null && value != null) {
            if (StringUtils.isNotBlank(dict.dictCode())) {
                String dictCode = dict.dictCode();
                return getDictText(dictCode, value, cache, timeout);
            } else if (StringUtils.isNotBlank(dict.dictExp())) {
                return getTextByDictExp(dict, value, cache, timeout);
            }
        }
        return null;
    }

    private static String getTextByDictExp(Dict dict, Object value, boolean cache, int timeout) {
        Map<String, String> map = getDictExp(dict, cache, timeout, false);
        return value == null ? null : map.get(String.valueOf(value));
    }

    public static SysDictItem getCacheDictItem(String dictCode, Object value, boolean cache, int timeout) {
        if (value == null) {
            return null;
        }
        Map<String, String> objectMap = getCacheDictMap(dictCode, cache, timeout, false);
        SysDictItem sysDictItem = JSONObject.parseObject(objectMap.get(String.valueOf(value)), SysDictItem.class);
        return sysDictItem;
    }

    private static Map<String, String> getCacheDictMap(String dictCode, boolean cache, int timeout, boolean reverse) {
        String mapKey = null;
        if (reverse) {
            mapKey = DictConstant.DICT_MAP_REVERSE_KEY + dictCode;
        } else {
            mapKey = DictConstant.DICT_MAP_KEY + dictCode;
        }
        String s = redisTemplate.opsForValue().get(mapKey);
        if (StringUtils.isNotBlank(s)) {
            return JSONObject.parseObject(s, Map.class);
        }

        Map<String, String> entries = new HashMap<>();
        List<SysDictItem> sysDictItemList = getCache(dictCode);
        for (SysDictItem sysDictItem : sysDictItemList) {
            String js = JSONObject.toJSONString(sysDictItem);
            if (reverse) {
                entries.put(sysDictItem.getText(), js);
            } else {
                entries.put(sysDictItem.getValue(), js);
            }
        }

        if (cache && timeout > 0) {
            redisTemplate.opsForValue().set(mapKey, JSONObject.toJSONString(entries), timeout, TimeUnit.SECONDS);
        }
        return entries;
    }

    private static Map<String, String> getDictExp(Dict dict, boolean cache, int timeout, boolean reverse) {
        Map<String, String> map = new HashMap<>();
        String dictExp = dict.dictExp();
        if (StringUtils.isNotBlank(dictExp)) {
            String expKey = null;
            if (reverse) {
                expKey = DictConstant.DICT_EXP_REVERSE_KEY + dictExp.hashCode();
            } else {
                expKey = DictConstant.DICT_EXP_KEY + dictExp.hashCode();
            }
            String s = redisTemplate.opsForValue().get(expKey);
            if (StringUtils.isNotBlank(s)) {
                return JSONObject.parseObject(s, Map.class);
            }

            String[] split = dictExp.split(dict.sepDict());
            for (String dictItem : split) {
                if (StringUtils.isNotBlank(dictItem)) {
                    String[] item = dictItem.split(dict.sepPair());
                    if (reverse) {
                        map.put(item[1], item[0]);
                    } else {
                        map.put(item[0], item[1]);
                    }
                }
            }
            if (!map.isEmpty() && cache && timeout > 0) {
                redisTemplate.opsForValue().set(expKey, JSONObject.toJSONString(map), timeout, TimeUnit.SECONDS);
            }
        }
        return map;
    }

    public static void removeAllCache() {
        Set<String> keys = redisTemplate.keys(DictConstant.DICT_KEY + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    public static void addCache(String dictCode, List<SysDictItem> sysDictItemList) {
        redisTemplate.opsForValue().set(DictConstant.DICT_KEY + dictCode, JSONArray.toJSONString(sysDictItemList));
    }

    public static void removeCache(String dictCode) {
        redisTemplate.delete(DictConstant.DICT_KEY + dictCode);
    }

    public static List<SysDictItem> getCache(String dictCode) {
        String s = redisTemplate.opsForValue().get(DictConstant.DICT_KEY + dictCode);
        if (StringUtils.isNotBlank(s)) {
            return JSONArray.parseArray(s, SysDictItem.class);
        } else {
            return new ArrayList<>();
        }
    }
}
