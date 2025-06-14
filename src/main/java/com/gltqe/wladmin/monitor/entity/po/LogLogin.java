package com.gltqe.wladmin.monitor.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("log_login")
public class LogLogin {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 登录用户id
     **/
    @TableField("uid")
    private String uid;

    /**
     * 用户名
     **/
    @TableField("username")
    private String username;

    /**
     * 部门id
     **/
    @TableField("dept_id")
    private String deptId;

    /**
     * ip地址
     **/
    @TableField("ip")
    private String ip;

    /**
     * 浏览器
     **/
    @TableField("browser")
    private String browser;

    /**
     * 操作系统
     **/
    @TableField("os")
    private String os;

    /**
     * 登录时间
     **/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("time")
    private Date time;

    /**
     * 登录类型 (暂不使用)
     **/
    @TableField("type")
    private String type;

    /**
     * 是否删除 0 未删除  1 已删除
     **/
    @TableField("is_delete")
    @TableLogic(value = "0", delval = "1")
    private Integer isDelete;


}
