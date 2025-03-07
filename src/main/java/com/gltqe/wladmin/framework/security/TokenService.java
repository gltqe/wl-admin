package com.gltqe.wladmin.framework.security;

import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.commons.exception.TokenErrorException;
import com.gltqe.wladmin.commons.exception.TokenExpireException;
import com.gltqe.wladmin.commons.utils.JwtUtil;
import com.gltqe.wladmin.system.entity.bo.UserDetailsBo;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;

/**
 * @author glate
 * @date 2023/5/17 8:50
 */
@Configuration
public class TokenService {


    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    public TokenUserDetails getSysUserDetails(String token){
        // 从jwt中获取username id
        boolean verify = JwtUtil.verifyToken(token);
        TokenUserDetails tokenUserDetails = new TokenUserDetails();
        tokenUserDetails.setFlag(false);
        if (!verify) {
            tokenUserDetails.setRuntimeException(new TokenErrorException("token异常，请重新登录"));
            return tokenUserDetails;
        }
        Boolean tokenExpired = JwtUtil.isTokenExpired(token);
        if (tokenExpired) {
            tokenUserDetails.setRuntimeException(new TokenExpireException("token已过期，请重新获取"));
            return tokenUserDetails;
        }

        // 查询redis中是否有该用户
        Object o = redisTemplate.opsForValue().get(Constant.LOGIN_USER_KEY + JwtUtil.getUsernameByToken(token));
        if (Objects.isNull(o)) {
            tokenUserDetails.setRuntimeException(new TokenErrorException("已被强制退出,请重新登录"));
            return tokenUserDetails;
        } else {
            UserDetailsBo userDetails =  (UserDetailsBo) o;
            tokenUserDetails.setFlag(true);
            tokenUserDetails.setSysUserDetails(userDetails);
        }
        return tokenUserDetails;
    }

    @Data
    public static class TokenUserDetails{
        private boolean flag;

        private UserDetailsBo sysUserDetails;

        private RuntimeException runtimeException;
    }
}
