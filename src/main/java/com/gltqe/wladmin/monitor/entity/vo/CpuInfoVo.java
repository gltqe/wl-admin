package com.gltqe.wladmin.monitor.entity.vo;

import lombok.Data;

@Data
public class CpuInfoVo {
    /**
     * CPU总数
     **/
    private String total;

    /**
     * 当前空闲率
     **/
    private String idleRate;

    /**
     * 系统使用率
     **/
    private String sysRate;

    /**
     * 用户使用率
     **/
    private String userRate;

    /**
     * 使用率
     **/
    private String usedRate;
}
