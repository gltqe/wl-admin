package com.gltqe.wladmin.commons.common;

/**
 * @author gltqe
 * @date 2024/9/17 1:52
 */
public class ConfigConstant {


    /**
     * Config key  配置key
     */
    public static final String CONFIG_KEY = "sysConfig:";


    /**
     * 是否验证码
     */
    public static final String HAS_CAPTCHA = CONFIG_KEY + "hasCaptcha";

    /**
     * 错误登录次数限制
     */
    public static final String ERROR_TIMES_LIMIT = CONFIG_KEY + "errorTimesLimit";

    /**
     * 错误登录锁定时间
     */
    public static final String LOGIN_LOCK_TIME = CONFIG_KEY + "loginLockTime";

}

