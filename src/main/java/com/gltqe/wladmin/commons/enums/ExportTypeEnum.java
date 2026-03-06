package com.gltqe.wladmin.commons.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导出类型
 *
 * @author gltqe
 * @date 2022/7/2 23:52
 **/
@Getter
public enum ExportTypeEnum {

    CURRENT_PAGE("current", "当前分页"),

    QUERY("query", "当前查询（默认）"),

    ALL("all", "全部数据"),

    SELECT("select", "勾选数据");

    private String code;
    private String desc;

    private static final Map<String, String> map;
    private static final List<String> list;

    static {
        map = new HashMap<>();
        list = new ArrayList<>();
        for (ExportTypeEnum value : ExportTypeEnum.values()) {
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

    public static boolean hasCode(Integer code) {
        return list.contains(code);
    }

    ExportTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    ExportTypeEnum() {
    }

}
