package com.gltqe.wladmin.system.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.gltqe.wladmin.commons.base.BaseDto;
import com.gltqe.wladmin.system.entity.po.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author gltqe
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserDto extends BaseDto<SysUser> {

    private String id;

    /**
     * 名字
     **/
    private String cnName;

    /**
     * 用户名
     **/
    private String username;

    /**
     * 密码
     **/
    private String password;

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
    private String sex;

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
     * 关联角色id
     **/
    @TableField(exist = false)
    private List<String> roleIds;

    /**
     * 职位名称
     **/
    @TableField(exist = false)
    private List<String> positionIds;

    /**
     * 头像
     */
//    private MultipartFile avatarFile;


}
