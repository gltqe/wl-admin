package com.gltqe.wladmin.monitor.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gltqe.wladmin.commons.base.BaseDto;
import com.gltqe.wladmin.monitor.entity.po.LogLogin;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class LoginLogDto extends BaseDto<LogLogin> {


    private String id;

    /**
     * 登录用户id
     **/
    private String uid;

    /**
     * 姓名
     **/
    private String cnName;

    /**
     * 用户名
     **/
    private String username;

    /**
     * 部门id
     **/
    private String deptId;

    /**
     * ip地址
     **/
    private String ip;

    /**
     * 浏览器
     **/
    private String browser;

    /**
     * 操作系统
     **/
    private String os;

    /**
     * 登录时间
     **/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

    /**
     * 登录类型 (暂不使用)
     **/
    private String type;

    /**
     * 是否删除 0 未删除  1 已删除
     **/
    private Integer isDelete;


    /**
     * 开始时间
     */
    private String startDateTime;

    /**
     * 结束时间
     */
    private String endDateTime;

}
