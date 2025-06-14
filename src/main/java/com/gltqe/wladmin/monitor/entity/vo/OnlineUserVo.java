package com.gltqe.wladmin.monitor.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author gltqe
 * @date 2024/3/16 14:15
 */
@Data
public class OnlineUserVo {
    private String userId;

    private String username;

    private String status;

    private String deptId;

    private String deptName;

    private Date loginTime;

    private String ip;

    private String browser;

    private String os;

    private String cnName;


}
