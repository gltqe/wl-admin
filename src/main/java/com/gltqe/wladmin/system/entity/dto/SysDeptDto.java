package com.gltqe.wladmin.system.entity.dto;


import lombok.Data;

import java.util.List;

/**
 * @author gltqe
 */
@Data
public class SysDeptDto {

//    @NotBlank(message = "id不能为空")
    private String id;
    /**
     * 父id
     **/
//    @NotBlank(message = "上级部门不能为空")
    private String parentId;

    /**
     * 部门名称
     **/
//    @NotBlank(message = "部门名称不能为空")
//    @Size(min = 1,max = 120,message = "菜单名称不能超过120个字符")
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

    /**
     * 简介
     **/
    private List<String> ids;

}
