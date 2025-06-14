package com.gltqe.wladmin.system.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gltqe.wladmin.commons.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author gltqe
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_user")
public class SysUser extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 名字
     **/
    @TableField("cn_name")
    private String cnName;

    /**
     * 用户名
     **/
    @TableField("username")
    private String username;

    /**
     * 密码
     **/
    @TableField(value = "password",select = false)
    private String password;

    /**
     * 盐
     **/
    @TableField(value = "salt",select = false)
    private String salt;

    /**
     * 所属部门id
     **/
    @TableField(value = "dept_id")
    private String deptId;

    /**
     * 手机号
     **/
    @TableField("phone")
    private String phone;

    /**
     * 邮箱
     **/
    @TableField("email")
    private String email;

    /**
     * 年龄
     **/
    @TableField("age")
    private Integer age;

    /**
     * 性别 0 女 1 男
     **/
    @TableField("sex")
    private Integer sex;

    /**
     * 账号状态 0 正常 1 拉黑  2 停用 3 锁定 4 过期
     **/
    @TableField("status")
    private String status;

    /**
     * 个人简介
     **/
    @TableField("profile")
    private String profile;

    /**
     * 备注
     **/
    @TableField("remarks")
    private String remarks;

    /**
     * 头像
     **/
    @TableField("avatar")
    private String avatar;



}
