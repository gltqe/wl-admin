package com.gltqe.wladmin.system.entity.vo;

import com.gltqe.wladmin.commons.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: gltqe
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysConfigVo extends BaseVo {
    /**
     * 名称
     **/
    private String name;

    /**
     * 编码
     **/
    private String code;

    /**
     * 值
     **/
    private String value;

    /**
     * 类型 0 系统参数 1 业务参数
     **/
    private String type;

    /**
     * 状态 0 启用 1 停用
     **/
    private String status;

    /**
     * 备注
     **/
    private String remarks;
}
