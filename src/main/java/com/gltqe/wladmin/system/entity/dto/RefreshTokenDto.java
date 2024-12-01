package com.gltqe.wladmin.system.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author gltqe
 * @date 2023/5/17 14:48
 */
@Data
public class RefreshTokenDto {
    @NotBlank(message = "刷新token不能为空")
    private String refreshToken;
}
