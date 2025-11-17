package com.gltqe.wladmin.framework.interceptor;

import com.alibaba.fastjson2.JSON;
import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.commons.common.Result;
import com.gltqe.wladmin.commons.exception.LimitException;
import com.gltqe.wladmin.commons.utils.IpUtil;
import com.gltqe.wladmin.commons.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * 接口访问次数拦截器
 *
 * @author gltqe
 * @date 2022/7/3 2:18
 **/
@Configuration
@ConditionalOnProperty(prefix = "cache",value = "type", havingValue = "redis")
public class ApiLimitInterceptor implements HandlerInterceptor {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedisScript<Long> limitScript;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uri = requestURI.replaceFirst(contextPath, "");
        boolean member = Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(Constant.LIMIT_URL_KEY, uri));
        if (member) {
            String singleKey = Constant.LIMIT_SINGLE_KEY + uri;
            String wholeKey = Constant.LIMIT_WHOLE_KEY + uri;
            String wholeLimiterKey = Constant.LIMIT_WHOLE_LIMITER_KEY + uri;
            String singleUserKey = "";
            // 不需登录接口 无法获取用户名 改为IP限制
            if (JwtUtil.isLogin()) {
                singleUserKey = singleKey + ":" + JwtUtil.getUsername();
            } else {
                singleUserKey = singleKey + ":" + IpUtil.getIpAddress(request);
            }
            long value = redisTemplate.execute(limitScript, Arrays.asList(singleKey, singleUserKey, wholeKey, wholeLimiterKey));
            if (Constant.LIMIT_SINGLE_EXCEED == value) {
                throw new LimitException("您的访问频率过高,请稍后尝试");
            }
            if (Constant.LIMIT_WHOLE_EXCEED == value) {
                throw new LimitException("当前接口访问频率过高,请稍后尝试");
            }
        }
        return true;
    }

    private void setResponse(HttpServletResponse response, String content) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(JSON.toJSONString(Result.error(content)));
        out.flush();
        out.close();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)  {

    }
}
