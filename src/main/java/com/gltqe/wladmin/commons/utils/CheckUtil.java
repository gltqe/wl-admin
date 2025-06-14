package com.gltqe.wladmin.commons.utils;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验工具类
 *
 * @author gltqe
 * @date 2022/7/2 23:53
 **/
public class CheckUtil {

    /**
     * 校验正则表达式
     *
     * @param rex
     * @param str
     * @return java.lang.Boolean
     * @author gltqe
     * @date 2022/7/3 0:06
     **/
    public static Boolean match(String rex, String str) {
        // String quote = Pattern.quote(rex);
        Pattern pattern = Pattern.compile(rex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 判断是否为空
     *
     * @param object
     * @return boolean
     * @author gltqe
     * @date 2022/7/3 0:08
     **/
    public static <T> boolean isEmpty(T object) {
        if (object == null) {
            return true;
        }
        if (object instanceof String) {
            return "".equals(object) || "null".equals(object);
        }
        if (object instanceof List) {
            return ((List<?>) object).isEmpty();
        }
        if (object instanceof Set) {
            return ((Set<?>) object).isEmpty();
        }
        return false;
    }

    /**
     * 判断一个对象是否不为空
     *
     * @param object object
     * @return boolean
     */
    public static boolean isNotEmpty(Object object) {
        return object != null && !"".equals(object) && !"null".equals(object);
    }

}
