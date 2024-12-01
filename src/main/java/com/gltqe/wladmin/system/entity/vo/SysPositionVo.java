package com.gltqe.wladmin.system.entity.vo;

import com.gltqe.wladmin.commons.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author gltqe
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysPositionVo extends BaseVo {

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
