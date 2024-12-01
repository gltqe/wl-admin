package com.gltqe.wladmin.commons.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志类型
 *
 * @author gltqe
 * @date 2022/7/2 23:52
 **/
public enum OperationLogTypeEnum {

    OTHER("0", "其他"),

    ADD("1", "新增"),

    REMOVE("2", "删除"),

    UPDATE("3", "修改"),

    QUERY("4", "查询"),

    IMPORT("5", "导入"),

    EXPORT("6", "导出");

    private String code;
    private String desc;

    private static final Map<String, String> map;
    private static final List<String> list;

    static {
        map = new HashMap<>();
        list = new ArrayList<>();
        for (OperationLogTypeEnum value : OperationLogTypeEnum.values()) {
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

    OperationLogTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    OperationLogTypeEnum() {
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
}
