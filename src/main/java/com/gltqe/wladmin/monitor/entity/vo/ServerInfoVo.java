package com.gltqe.wladmin.monitor.entity.vo;

import lombok.Data;

@Data
public class ServerInfoVo {
    /**
     * 服务器名称
     **/
    private String serverName;

    /**
     * 服务器地址
     **/
    private String serverIp;

    /**
     * 操作系统名称
     **/
    private String osName;

    /**
     * 操作系统架构
     **/
    private String osArch;
    /**
     * java版本
     **/
    private String javaVersion;

    /**
     * jvm名称
     **/
    private String jvmName;

    /**
     * jvm版本
     **/
    private String jvmVersion;

    /**
     * 项目路径
     **/
    private String projectDir;

    /**
     * 运行参数
     **/
    private String runParameters;
}
