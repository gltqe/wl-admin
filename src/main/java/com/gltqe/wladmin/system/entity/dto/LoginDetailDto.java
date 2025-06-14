package com.gltqe.wladmin.system.entity.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author gltqe
 * @date 2024/3/18 13:26
 */
@Data
public class LoginDetailDto {

    private Date loginTime;

    private String ip;

    private String browser;

    private String os;
}
