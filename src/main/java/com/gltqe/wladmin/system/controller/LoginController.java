package com.gltqe.wladmin.system.controller;

import com.gltqe.wladmin.commons.common.Result;
import com.gltqe.wladmin.system.entity.vo.CaptchaVo;
import com.gltqe.wladmin.system.entity.vo.LoginVo;
import com.gltqe.wladmin.system.entity.dto.LoginDto;
import com.gltqe.wladmin.system.entity.dto.RefreshTokenDto;
import com.gltqe.wladmin.system.service.LoginService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 登录
 *
 * @author gltqe
 * @date 2022/7/3 1:24
 **/
@RestController
public class LoginController {

    @Resource
    private LoginService loginService;

    /**
     * 获取验证码
     *
     * @return Result
     * @author gltqe
     * @date 2023/5/16 11:25
     **/
    @RequestMapping("/getCaptcha")
    public Result getCaptcha() {
        CaptchaVo captchaDto = loginService.getCaptcha();
        return Result.ok(captchaDto);
    }


    /**
     * 登录接口
     *
     * @param loginDto
     * @param request
     * @return Result
     * @author gltqe
     * @date 2023/5/16 11:27
     **/
    @RequestMapping("/login")
    public Result login(@RequestBody LoginDto loginDto, HttpServletRequest request) {
        LoginVo loginVo = loginService.login(loginDto, request);
        return Result.ok("登录成功", loginVo);
    }

    /**
     * 刷新accessToken
     * @param refreshTokenDto
     * @return: com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2023/5/17 15:13
     **/
    @RequestMapping("/refreshToken")
    public Result refreshToken(@RequestBody RefreshTokenDto refreshTokenDto, HttpServletRequest request) {
        String accessToken = loginService.refreshToken(refreshTokenDto,request);
        return Result.ok("刷新token成功",accessToken);
    }
}
