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
@TableName("sys_dict")
public class SysDict extends BaseEntity {
    /**
     * 字典名称
     **/
//    @NotBlank(message = "字典名称不能为空")
//    @Size(min = 1,max = 60,message = "字典名称不能超过60个字符")
    @TableField("name")
    private String name;

    /**
     * 字典编码
     **/
//    @NotBlank(message = "字典编码不能为空")
//    @Size(min = 1,max = 60,message = "字典编码不能超过60个字符")
    @TableField("code")
    private String code;

    /**
     * 状态
     **/
    @TableField("status")
    private String status;

    /**
     * 备注
     **/
    @TableField("remarks")
    private String remarks;

}
