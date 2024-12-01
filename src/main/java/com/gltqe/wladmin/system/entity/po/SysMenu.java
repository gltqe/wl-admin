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
@TableName("sys_menu")
public class SysMenu extends TreeVo<SysMenu> {

    /**
     * 父id
     **/
//    @NotBlank(message = "上级菜单不能为空")
    @TableField("parent_id")
    private String parentId;

    /**
     * 名称
     **/
//    @NotBlank(message = "菜单名称不能为空")
//    @Size(min = 1,max = 120,message = "菜单名称不能超过120个字符")
    @TableField("name")
    private String name;

    /**
     * 路由
     **/
    @TableField("path")
    private String path;

    /**
     * 组件路径
     **/
    @TableField("component")
    private String component;

    /**
     * 组件名称
     **/
    @TableField("component_name")
    private String componentName;

    /**
     * 菜单类型 0 目录  1 菜单  2 按钮
     **/
//    @NotBlank(message = "菜单类型不能为空")
    @TableField("type")
    private String type;

    /**
     * 权限标识
     **/
//    @NotBlank(message = "权限标识不能为空")
//    @Size(min = 1,max = 120,message = "权限标识不能超过120个字符")
    @TableField("permission")
    private String permission;

    /**
     * 排序
     **/
//    @NotNull(message = "排序不能为空")
    @TableField("sort")
    private Integer sort;

    /**
     * 标志
     **/
    @TableField("icon_type")
    private String iconType;

    /**
     * 状态 0 显示 1 隐藏
     **/
    @TableField("status")
    private String status;

    /**
     * 子菜单
     **/
    //@TableField(exist = false)
    //private List<SysMenu> children;
}
