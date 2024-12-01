package com.gltqe.wladmin.system.entity.vo;

import com.gltqe.wladmin.commons.base.TreeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author gltqe
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDeptVo extends TreeVo<SysDeptVo> {
    /**
     * 父id
     **/
    private String parentId;

    /**
     * 上级id集合
     **/
    private String higherLevel;

    /**
     * 部门名称
     **/
    private String name;


    /**
     * 负责人
     **/
    private String leader;

    /**
     * 排序
     **/
    private Integer sort;

    /**
     * 电话
     **/
    private String phone;

    /**
     * 邮箱
     **/
    private String email;

    /**
     * 状态 0 正常 1 停用
     **/
    private String status;

    /**
     * 简介
     **/
    private String summary;

}
