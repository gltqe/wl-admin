package com.gltqe.wladmin.commons.utils;

import com.alibaba.fastjson2.JSONArray;
import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.framework.excel.Dict;
import com.gltqe.wladmin.system.entity.po.SysDictItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author gltqe
 * @date 2025/2/20 8:54
 */
public class DictUtil {

    private static final RedisTemplate<String, String> redisTemplate = SpringContextUtil.getBean(RedisTemplate.class);

    private static final String DICT_MAP_KEY = "dictMap:";

    private static final String DICT_EXP_KEY = "dictExp:";

    /**
     * 默认map是否缓存
     */
    private static final boolean DEFAULT_CACHE = true;

    /**
     * 默认map缓存时间
     */
    private static final int DEFAULT_TIMEOUT = 5;

    public static String getDictText(String dictCode, Object value) {
        return getDictText(dictCode, value, DEFAULT_CACHE);
    }

    public static String getDictText(String dictCode, Object value, boolean cache) {
        return getDictText(dictCode, value, cache, DEFAULT_TIMEOUT);
    }

    public static String getDictText(String dictCode, Object value, boolean cache, int timeout) {
        if (value == null) {
            return null;
        }
        Map<Object, Object> objectMap = refreshDictMap(dictCode, cache, timeout, false);
        Object o = objectMap.get(String.valueOf(value));
        return o == null ? null : String.valueOf(o);
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
        String dictExp = dict.dictExp();
        String[] split = dictExp.split(dict.sepDict());
        String result = null;
        Map<String, String> map = new HashMap<>();
        for (String dictItem : split) {
            if (StringUtils.isNotBlank(dictItem)) {
                String[] item = dictItem.split(dict.sepPair());
                if (String.valueOf(value).equals(item[0])) {
                    result = item[1];
                }
                map.put(item[0], item[1]);
            }
        }
        refreshDictExp(dictExp, map, cache, timeout);
        return result;
    }

    private static Map<Object, Object> refreshDictMap(String dictCode, boolean cache, int timeout, boolean reverse) {
        String mapKey = DICT_MAP_KEY + dictCode;
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(mapKey);
        if (entries.isEmpty()) {
            String s = redisTemplate.opsForValue().get(Constant.DICT_KEY + dictCode);
            if (StringUtils.isNotBlank(s)) {
                List<SysDictItem> sysDictItems = JSONArray.parseArray(s, SysDictItem.class);
                Map<Object, Object> map = new HashMap<>();
                for (SysDictItem sysDictItem : sysDictItems) {
                    if (reverse) {
                        map.put(sysDictItem.getText(), sysDictItem.getValue());
                    } else {
                        map.put(sysDictItem.getValue(), sysDictItem.getText());
                    }
                }
                if (cache && timeout > 0) {
                    redisTemplate.opsForHash().putAll(mapKey, entries);
                    redisTemplate.expire(mapKey, timeout, TimeUnit.SECONDS);
                }
                return map;
            }
        }
        return entries;
    }

    private static void refreshDictExp(String dictExp, Map<String, String> map, boolean cache, int timeout) {
        if (!map.isEmpty() && cache && timeout > 0) {
            String expKey = DICT_EXP_KEY + dictExp.hashCode();
            redisTemplate.opsForHash().putAll(expKey, map);
            redisTemplate.expire(expKey, timeout, TimeUnit.SECONDS);
        }
    }

}
