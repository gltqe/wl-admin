package com.gltqe.wladmin.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gltqe.wladmin.commons.base.TreeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author gltqe
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dept")
public class SysDept extends TreeVo<SysDept> {
    /**
     * 父id
     **/
//    @NotBlank(message = "上级部门不能为空")
    @TableField("parent_id")
    private String parentId;

    /**
     * 上级id集合
     **/
    @TableField("higher_level")
    private String higherLevel;

    /**
     * 部门名称
     **/
//    @NotBlank(message = "部门名称不能为空")
//    @Size(min = 1,max = 120,message = "菜单名称不能超过120个字符")
    @TableField("name")
    private String name;


    /**
     * 负责人
     **/
    @TableField("leader")
    private String leader;

    /**
     * 排序
     **/
    @TableField("sort")
    private Integer sort;

    /**
     * 电话
     **/
    @TableField("phone")
    private String phone;

    /**
     * 邮箱
     **/
    @TableField("email")
    private String email;

    /**
     * 状态 0 正常 1 停用
     **/
    @TableField("status")
    private String status;

    /**
     * 简介
     **/
    @TableField("summary")
    private String summary;

}
