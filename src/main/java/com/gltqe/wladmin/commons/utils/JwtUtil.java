package com.gltqe.wladmin.commons.utils;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.system.config.JwtProperties;
import com.gltqe.wladmin.system.entity.bo.UserDetailsBo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

/**
 * token工具类
 *
 * @author gltqe
 * @date 2022/7/3 0:2
 **/
@Slf4j
public class JwtUtil {


    private static final JwtProperties properties = SpringContextUtil.getBean(JwtProperties.class);

    /**
     * 创建token
     *
     * @param
     * @param
     * @return java.lang.String
     * @author gltqe
     * @date 2022/7/3 0:27
     **/
    public static String createToken(Map<String, Object> map, Long ttlSecond) {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + ttlSecond * 1000;
        map.put(Constant.EXPIRE_TIME_KEY, expMillis);
        // getSigner可以根据type选择签名方式
        String token = JWTUtil.createToken(map, properties.getHs256Signer());
        return token;
    }

    /**
     * 校验token
     *
     * @param token
     * @return boolean
     * @author gltqe
     * @date 2023/5/16 11:28
     **/
    public static boolean verifyToken(String token) {
        if (StringUtils.isBlank(token)) {
            return false;
        }
        try {
            return JWTUtil.verify(token, properties.getHs256Signer());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 解析token
     *
     * @param token
     * @return JWTPayload
     * @author gltqe
     * @date 2023/5/16 11:49
     **/
    public static JWTPayload parseToken(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        JWTPayload payload = jwt.getPayload();
        return payload;
    }

    /**
     * 获取token
     *
     * @return java.lang.String
     * @author gltqe
     * @date 2022/7/3 0:28
     **/
    public static String getToken() {
        String token = SpringContextUtil.getHttpServletRequest().getHeader(Constant.TOKEN_HEAD);
        return token;
    }

    /**
     * 判断是否登录
     *
     * @return java.lang.String
     * @author gltqe
     * @date 2022/7/3 0:28
     **/
    public static Boolean isLogin() {
        try {
            UserDetailsBo userDetails = getUserDetails();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取用户名
     *
     * @return java.lang.String
     * @author gltqe
     * @date 2022/7/3 0:28
     **/
    public static String getUsername() {
        UserDetailsBo userDetails = getUserDetails();
        String username = userDetails.getUsername();
        return username;
    }

    public static String getUsernameByToken(String token) {
        JWTPayload jwtPayload = parseToken(token);
        Object username = jwtPayload.getClaim("username");
        return username == null ? null : String.valueOf(username);
    }

    /**
     * 获取用户id
     *
     * @return java.lang.String
     * @author gltqe
     * @date 2022/7/3 0:29
     **/
    public static String getUserId() {
        UserDetailsBo userDetails = getUserDetails();
        String userId = userDetails.getUserId();
        return userId;

    }


    /**
     * 获取当前登录用户部门ID
     *
     * @return java.lang.String
     * @author gltqe
     * @date 2022/7/3 0:29
     **/
    public static String getDeptId() {
        UserDetailsBo userDetails = getUserDetails();
        String deptId = userDetails.getDeptId();
        return deptId;
    }

    /**
     * 获取过期时间
     *
     * @return java.lang.Long
     * @author gltqe
     * @date 2022/7/3 0:29
     **/
    public static Long getExpireTime() {
        String token = SpringContextUtil.getHttpServletRequest().getHeader(Constant.TOKEN_HEAD);
        return getExpireTime(token);
    }

    public static Long getExpireTime(String token) {
        JWTPayload jwtPayload = parseToken(token);
        Object expireTime = jwtPayload.getClaim(Constant.EXPIRE_TIME_KEY);
        return expireTime == null ? -1 : Long.parseLong(expireTime.toString());
    }

    /**
     * 判断是否过期
     *
     * @return Boolean
     * @author gltqe
     * @date 2023/5/16 13:50
     **/
    public static Boolean isTokenExpired() {
        String token = SpringContextUtil.getHttpServletRequest().getHeader(Constant.TOKEN_HEAD);
        return isTokenExpired(token);
    }

    public static Boolean isTokenExpired(String token) {
        Long expireTime = getExpireTime(token);
        long l = System.currentTimeMillis();
        return expireTime <= l;
    }

    /**
     * 获取认证用户
     *
     * @return com.gltqe.wladmin.system.entity.bo.SysUserDetails
     * @author gltqe
     * @date 2022/7/3 0:29
     **/
    public static UserDetailsBo getUserDetails() {
        return (UserDetailsBo) getAuthentication().getPrincipal();
    }

    /**
     * 获取Authentication认证信息
     *
     * @return org.springframework.security.core.Authentication
     * @author gltqe
     * @date 2022/7/3 0:30
     **/
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 判断是否 是admin用户
     *
     * @return boolean
     * @author gltqe
     * @date 2022/7/3 0:30
     **/
    public static boolean isAdmin() {
        return Constant.ADMIN.equals(getUsername());
    }

    public static boolean isAdmin(String username) {
        return Constant.ADMIN.equals(username);
    }

    /**
     * 获取token类型
     *
     * @param token
     * @return: java.lang.Integer
     * @author gltqe
     * @date 2023/5/17 15:24
     **/
    public static Integer getTokenType(String token) {
        JWTPayload jwtPayload = parseToken(token);
        Integer type = jwtPayload.getClaimsJson().getInt(Constant.TOKEN_TYPE_KEY);
        return type;
    }
}
