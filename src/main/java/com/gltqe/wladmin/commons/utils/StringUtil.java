package com.gltqe.wladmin.commons.utils;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 字符串工具类
 *
 * @author gltqe
 * @date 2022/7/3 0:32
 **/
public class StringUtil {
    /**
     * 获取UUID
     *
     * @return java.lang.String
     * @author gltqe
     * @date 2022/7/3 0:33
     **/
    public static String getUUId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 列表转字符串
     *
     * @param list
     * @param sep
     * @return java.lang.String
     * @author gltqe
     * @date 2022/7/3 0:33
     **/
    public static String listToStr(List<String> list, String sep) {
        if (sep == null) {
            sep = ",";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            sb.append(s);
            if (i != list.size() - 1) {
                sb.append(sep);
            }
        }
        return sb.toString();
    }

    /**
     * 字符串转列表
     *
     * @param source
     * @param sep
     * @return java.lang.String
     * @author gltqe
     * @date 2022/7/3 0:33
     **/
    public static List<String> strToList(String source, String sep) {
        if (sep == null) {
            sep = ",";
        }
        String[] split = source.split(sep);
        List<String> list = Arrays.asList(split);
        return list;
    }
}
