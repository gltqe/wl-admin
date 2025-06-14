package com.gltqe.wladmin.system.entity.vo;

import lombok.Data;

@Data
public class LoginVo {
    /**
     * 鉴权token
     */
    private String accessToken;

    /**
     * 刷新token
     */
    private String refreshToken;

}
