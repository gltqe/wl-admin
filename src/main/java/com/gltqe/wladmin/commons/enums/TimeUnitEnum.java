package com.gltqe.wladmin.commons.enums;

import com.gltqe.wladmin.commons.exception.WlException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件类型
 *
 * @author gltqe
 * @date 2022/7/2 23:51
 **/

public enum TimeUnitEnum {

    DAY("0", "天", 24 * 60 * 60 * 1000L * 1000L),

    HOUR("1", "小时", 60 * 60 * 1000L * 1000L),

    MINUTE("2", "分钟", 60 * 1000L * 1000L),

    SECOND("3", "秒", 1000L * 1000L),

    MILLISECOND("4", "毫秒", 1000L),

    MICROSECOND("5", "微秒", 1L);

    private String code;
    private String desc;
    private Long value;

    private static final Map<String, String> map;
    private static final Map<String, Long> valueMap;
    private static final List<String> list;

    static {
        map = new HashMap<>();
        valueMap = new HashMap<>();
        list = new ArrayList<>();
        for (TimeUnitEnum value : TimeUnitEnum.values()) {
            map.put(value.getCode(), value.getDesc());
            valueMap.put(value.getCode(), value.getValue());
            list.add(value.getCode());
        }
    }

    public static String getDesc(String code) {
        String desc = map.get(code);
        if (desc == null) {
            return "";
        }
        return desc;
    }

    public static Long getValue(String code) {
        Long value = valueMap.get(code);
        if (value == null) {
            throw new WlException("时间枚举异常");
        }
        return value;
    }

    public static boolean hasCode(String code) {
        return list.contains(code);
    }

    TimeUnitEnum(String code, String desc, Long value) {
        this.code = code;
        this.desc = desc;
        this.value = value;
    }

    TimeUnitEnum() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
