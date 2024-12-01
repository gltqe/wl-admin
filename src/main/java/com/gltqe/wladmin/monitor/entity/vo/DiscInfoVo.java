package com.gltqe.wladmin.monitor.entity.vo;

import lombok.Data;

@Data
public class DiscInfoVo {
    /**
     * 总空间
     **/
    private String total;

    /**
     * 可用空间
     **/
    private String free;

    /**
     * 已用空间
     **/
    private String used;

    /**
     * 使用率
     **/
    private String rate;

}
