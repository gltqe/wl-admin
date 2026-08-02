package com.gltqe.wladmin.framework.interceptor;

import com.alibaba.fastjson2.JSON;
import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.commons.common.Result;
import com.gltqe.wladmin.commons.exception.LimitException;
import com.gltqe.wladmin.commons.utils.IpUtil;
import com.gltqe.wladmin.commons.utils.JwtUtil;
import com.gltqe.wladmin.framework.cache.CacheUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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
@ConditionalOnProperty(prefix = "cache", value = "type", havingValue = "redis")
@Order(2)
public class WholeApiLimitInterceptor implements HandlerInterceptor {

    @Resource
    private RedisScript<Long> limitScript;


    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestURI = request.getRequestURI();
        String uri = requestURI.replaceFirst(contextPath, "");
        String wholeKey = Constant.LIMIT_WHOLE_KEY + uri;
        String wholeLimiterKey = Constant.LIMIT_WHOLE_LIMITER_KEY + uri;
        // 不需登录接口 无法获取用户名 改为IP限制
        long value = CacheUtil.execute(limitScript, Arrays.asList(wholeKey, wholeLimiterKey));
        if (Constant.LIMIT_EXCEED == value) {
            throw new LimitException("当前接口访问频率过高,请稍后尝试");
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
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}
