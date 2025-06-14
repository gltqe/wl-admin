package com.gltqe.wladmin.system.entity.dto;

import com.gltqe.wladmin.commons.base.BaseDto;
import com.gltqe.wladmin.system.entity.po.SysPosition;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author gltqe
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysPositionDto extends BaseDto<SysPosition> {

    /**
     * 职位编码
     **/
    private String code;

    /**
     * 职位名称
     **/
    private String name;

    /**
     * 排序
     **/
    private Integer sort;

    /**
     * 状态 0 启用 1 停用
     **/
    private String status;

    /**
     * 备注
     **/
    private String remarks;


}
