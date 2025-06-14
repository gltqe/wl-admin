package com.gltqe.wladmin.system.entity.dto;

import com.gltqe.wladmin.commons.base.BaseDto;
import com.gltqe.wladmin.system.entity.po.SysConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author gltqe
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysConfigDto extends BaseDto<SysConfig> {
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
