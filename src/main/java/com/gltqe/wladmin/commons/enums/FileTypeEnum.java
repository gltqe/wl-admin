package com.gltqe.wladmin.commons.enums;

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
public enum FileTypeEnum {

    IMAGE(0, "图片"),

    EXCEL(1, "表格"),

    WORD(2, "文本"),

    PDF(3, "PDF"),

    OTHER(4, "其他");

    private Integer code;
    private String desc;

    private static final Map<Integer, String> map;
    private static final List<Integer> list;

    static {
        map = new HashMap<>();
        list = new ArrayList<>();
        for (FileTypeEnum value : FileTypeEnum.values()) {
            map.put(value.getCode(), value.getDesc());
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

    public static boolean hasCode(String code) {
        return list.contains(code);
    }

    FileTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    FileTypeEnum() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
