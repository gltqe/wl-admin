package com.gltqe.wladmin.monitor.entity.vo;

import lombok.Data;

@Data
public class MemoryInfoVo {

    /**
     * 内存总量
     **/
    private String total;

    /**
     * 已使用内存
     **/
    private String used;

    /**
     * 可用内存
     **/
    private String free;

    /**
     * 使用率
     **/
    private String rate;
}
