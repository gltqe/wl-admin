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
@TableName("sys_config")
public class SysConfig extends BaseEntity {
    /**
     * 名称
     **/
//    @NotBlank(message = "参数名称不能为空")
//    @Size(min = 1,max = 60,message = "参数名称不能超过60个字符")
    @TableField("name")
    private String name;

    /**
     * 编码
     **/
//    @NotBlank(message = "参数编码不能为空")
//    @Size(min = 1,max = 60,message = "参数编码不能超过60个字符")
    @TableField("code")
    private String code;

    /**
     * 值
     **/
//    @NotBlank(message = "参数值不能为空")
//    @Size(min = 1,max = 60,message = "参数编码不能超过60个字符")
    @TableField("value")
    private String value;

    /**
     * 类型 0 系统参数 1 业务参数
     **/
    @TableField("type")
    private String type;

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
