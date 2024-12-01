package com.gltqe.wladmin.system.entity.dto;

import lombok.Data;


/**
 * @author gltqe
 */
@Data
public class SysMenuDto {
//    @NotBlank(message = "id不能为空")
    private String id;

    /**
     * 父id
     **/
//    @NotBlank(message = "上级菜单不能为空" )
    private String parentId;

    /**
     * 名称
     **/
//    @NotBlank(message = "菜单名称不能为空")
//    @Size(min = 1,max = 120,message = "菜单名称不能超过120个字符")
    private String name;

    /**
     * 路由
     **/
    private String path;

    /**
     * 组件路径
     **/
    private String component;

    /**
     * 组件名称
     **/
    private String componentName;

    /**
     * 菜单类型 0 目录  1 菜单  2 按钮
     **/
//    @NotNull(message = "菜单类型不能为空")
    private String type;

    /**
     * 权限标识
     **/
//    @NotBlank(message = "权限标识不能为空")
//    @Size(min = 1,max = 120,message = "权限标识不能超过120个字符")
    private String permission;

    /**
     * 排序
     **/
//    @NotNull(message = "排序不能为空")
    private Integer sort;

    /**
     * 标志
     **/
    private String iconType;

    /**
     * 状态 0 显示 1 隐藏
     **/
    private String status;


}
