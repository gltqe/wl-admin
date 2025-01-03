package com.gltqe.wladmin.system.entity.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.gltqe.wladmin.commons.base.BaseVo;
import com.gltqe.wladmin.system.entity.po.SysRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 系统用户
 * @author gltqe
 * @date 2023/5/19 14:51
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelIgnoreUnannotated
public class SysUserVo extends BaseVo {

    private String id;

    /**
     * 名字
     **/
    @ExcelProperty("姓名")
    private String cnName;

    /**
     * 用户名
     **/
    private String username;

    /**
     * 所属部门id
     **/
    private String deptId;

    /**
     * 手机号
     **/
    private String phone;

    /**
     * 邮箱
     **/
    private String email;

    /**
     * 年龄
     **/
    private Integer age;

    /**
     * 性别 0 女 1 男
     **/
    private Integer sex;

    /**
     * 账号状态 0 正常 1 拉黑  2 停用 3 锁定 4 过期
     **/
    private String status;

    /**
     * 个人简介
     **/
    private String profile;

    /**
     * 备注
     **/
    private String remarks;

    /**
     * 头像
     **/
    private String avatar;

    /**
     * 头像
     **/
    private String avatarName;

    /**
     * 关联角色
     **/
    private List<SysRole> roleList;

    /**
     * 关联角色id
     **/
    private List<String> roleIds;

    /**
     * 部门名称
     **/
    private String deptName;

    /**
     * 职位名称
     **/
    private String positionName;

    /**
     * 职位名称
     **/
    private List<String> positionIds;

}
