package com.gltqe.wladmin.commons.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户状态
 *
 * @author gltqe
 * @date 2022/7/2 23:52
 **/
public enum UserStatusEnum {

    NORMAL("0", "正常"),

    DISABLE("1", "禁用"),

    LOCK("2", "锁定"),

    EXPIRED("3", "过期");

    private String code;
    private String desc;

    private static final Map<String, String> map;
    private static final List<String> list;

    static {
        map = new HashMap<>();
        list = new ArrayList<>();
        for (UserStatusEnum value : UserStatusEnum.values()) {
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

    UserStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    UserStatusEnum() {
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
