package com.gltqe.wladmin.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gltqe.wladmin.commons.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author gltqe
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_position")
public class SysPosition  extends BaseEntity {

    /**
     * 职位编码
     **/
    @TableField("code")
    private String code;

    /**
     * 职位名称
     **/
    @TableField("name")
    private String name;

    /**
     * 排序
     **/
    @TableField("sort")
    private Integer sort;

    /**
     * 状态 0 启用 1 停用
     **/
    @TableField("status")
    private String status;

    /**
     * 备注
     **/
    @TableField("remarks")
    private String remarks;


}
