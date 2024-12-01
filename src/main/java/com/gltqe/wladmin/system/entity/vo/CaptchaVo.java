package com.gltqe.wladmin.system.entity.vo;

import lombok.Data;


/**
 * @author gltqe
 */
@Data
public class CaptchaVo {
    /**
     * 验证码key
     */
    private String captchaKey;

    /**
     * 验证码
     */
    private String captchaValue;

    /**
     * 是否有验证码
     */
    private Boolean hasCaptcha;
}
