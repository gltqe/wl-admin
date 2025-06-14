package com.gltqe.wladmin.framework.security;


import com.alibaba.fastjson2.JSONObject;
import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.commons.common.Result;
import com.gltqe.wladmin.commons.utils.SpringContextUtil;
import com.gltqe.wladmin.commons.utils.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 登出成功处理
 * @author gltqe
 * @date 2022/7/3 1:05
 **/
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = JwtUtil.getUsernameByToken(request.getHeader(Constant.TOKEN_HEAD));
        SpringContextUtil.getBean(RedisTemplate.class).delete(Constant.LOGIN_USER_KEY + username);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(JSONObject.toJSONString(Result.ok("退出登录成功")));
        out.flush();
        out.close();
    }
}
