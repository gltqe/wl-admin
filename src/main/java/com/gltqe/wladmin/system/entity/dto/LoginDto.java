package com.gltqe.wladmin.system.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class LoginDto {

//    @NotBlank(message = "验证码错误")
    private String captchaKey;

//    @NotBlank(message = "验证码错误")
    private String captchaValue;

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

}
