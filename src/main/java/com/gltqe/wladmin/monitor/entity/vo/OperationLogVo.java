package com.gltqe.wladmin.monitor.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gltqe.wladmin.commons.base.BaseVo;
import lombok.Data;

import java.util.Date;

@Data

public class OperationLogVo extends BaseVo {


    private String id;

    /**
     * 操作名称
     **/
    private String name;

    /**
     * 操作类型
     **/
    private String type;

    /**
     * 方法名
     **/
    private String methodName;

    /**
     * 请求方式
     **/
    private String requestType;

    /**
     * 请求uri
     **/
    private String requestUri;

    /**
     * 请求IP地址
     **/
    private String requestIp;

    /**
     * 请求参数
     **/
    private String requestParams;

    /**
     * 响应结果
     **/
    private String responseResult;

    /**
     * 0 成功 1 异常
     **/
    private String status;

    /**
     * 错误信息
     **/
    private String errorMsg;

    /**
     * 请求时间
     **/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date requestTime;

    /**
     * 响应时间
     **/
    private Date responseTime;

    /**
     * 执行时间
     **/
    private String executeTime;

    /**
     * 操作人
     **/
    private String operator;

    /**
     * 操作人部门ID
     **/
    private String deptId;

    /**
     * 是否删除 0 未删除  1 已删除
     **/
    private Integer isDelete;

    /**
     * 用户姓名
     **/
    private String cnName;

    /**
     * 部门名称
     **/
    private String deptName;

}
