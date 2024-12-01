package com.gltqe.wladmin.monitor.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("log_operation")
public class LogOperation {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 操作名称
     **/
    @TableField("name")
    private String name;

    /**
     * 操作类型
     **/
    @TableField("type")
    private String type;

    /**
     * 方法名
     **/
    @TableField("method_name")
    private String methodName;

    /**
     * 请求方式
     **/
    @TableField("request_type")
    private String requestType;

    /**
     * 请求uri
     **/
    @TableField("request_uri")
    private String requestUri;

    /**
     * 请求IP地址
     **/
    @TableField("request_ip")
    private String requestIp;

    /**
     * 请求参数
     **/
    @TableField("request_params")
    private String requestParams;

    /**
     * 响应结果
     **/
    @TableField("response_result")
    private String responseResult;

    /**
     * 0 成功 1 异常
     **/
    @TableField("status")
    private String status;

    /**
     * 错误信息
     **/
    @TableField("error_msg")
    private String errorMsg;

    /**
     * 请求时间
     **/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("request_time")
    private Date requestTime;

    /**
     * 响应时间
     **/
    @TableField("response_time")
    private Date responseTime;

    /**
     * 执行时间
     **/
    @TableField("execute_time")
    private String executeTime;

    /**
     * 操作人
     **/
    @TableField("operator")
    private String operator;

    /**
     * 操作人部门ID
     **/
    @TableField("dept_id")
    private String deptId;

    /**
     * 是否删除 0 未删除  1 已删除
     **/
    @TableField("is_delete")
    @TableLogic(value = "0", delval = "1")
    private Integer isDelete;


}
