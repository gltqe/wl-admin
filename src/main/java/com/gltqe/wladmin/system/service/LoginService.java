package com.gltqe.wladmin.system.service;

import com.gltqe.wladmin.system.entity.vo.CaptchaVo;
import com.gltqe.wladmin.system.entity.vo.LoginVo;
import com.gltqe.wladmin.system.entity.dto.LoginDto;
import com.gltqe.wladmin.system.entity.dto.RefreshTokenDto;
import jakarta.servlet.http.HttpServletRequest;

public interface LoginService {

    CaptchaVo getCaptcha();

    /**
     * 登录接口
     * @author gltqe
     * @date 2022/7/3 1:57
     * @param loginDto
     * @param request
     * @return java.lang.String
     **/
    public LoginVo login(LoginDto loginDto, HttpServletRequest request);


    /**
     * 刷新token
     * @param refreshTokenDto
     * @return: java.lang.String
     * @author gltqe
     * @date 2023/5/17 14:54
     **/
    String refreshToken(RefreshTokenDto refreshTokenDto, HttpServletRequest request);

}
