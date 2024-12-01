package com.gltqe.wladmin.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gltqe.wladmin.commons.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author gltqe
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class SysRole extends BaseEntity {
    /**
     * 角色名称
     **/
    @TableField("name")
    private String name;

    /**
     * 角色编码
     **/
    @TableField("code")
    private String code;

    /**
     * 显示顺序
     **/
    @TableField("sort")
    private Integer sort;


    /**
     * 数据范围 0 本人数据 1 全部数据 2 本部门数据 3 本部门及以下数据 4 自定义权限
     **/
    @TableField("data_scope")
    private String dataScope;

    /**
     * 角色状态 0 正常 1 停用
     **/
    @TableField("status")
    private String status;

    /**
     * 备注
     **/
    @TableField("remark")
    private String remark;


    /**
     * 所属部门
     **/
    @TableField("create_dept")
    private String createDept;

    /**
     * 关联的全部菜单id  --- 所有可见可操作菜单
     **/
    @TableField(exist = false)
    private List<String> menuIds;

    /**
     * 选中菜单id   ---  当前角色 选中的菜单
     **/
    @TableField(exist = false)
    private List<String> checkedMenuIds;

    /**
     * 关联的全部部门id  --- 所有可选部门 当前用户关联的所有部门
     **/
    @TableField(exist = false)
    private List<String> deptIds;
    /**
     * 选中部门id     ---   当前角色关联的部门
     **/
    @TableField(exist = false)
    private List<String> checkedDeptIds;
}
