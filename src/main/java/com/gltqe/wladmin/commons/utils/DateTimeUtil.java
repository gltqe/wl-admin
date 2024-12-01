package com.gltqe.wladmin.commons.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
    /**
     * 获取今天的日期
     * @return
     */
    public static String getDateNow(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(new Date());
        return format;
    }
}
