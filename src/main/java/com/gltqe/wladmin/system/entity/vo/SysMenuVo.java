package com.gltqe.wladmin.system.entity.vo;

import com.gltqe.wladmin.commons.base.TreeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author gltqe
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenuVo extends TreeVo<SysMenuVo> {

    /**
     * 父id
     **/
    private String parentId;

    /**
     * 名称
     **/
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
    private String type;

    /**
     * 权限标识
     **/
    private String permission;

    /**
     * 排序
     **/
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
