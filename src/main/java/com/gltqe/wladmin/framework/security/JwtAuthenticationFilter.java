package com.gltqe.wladmin.framework.security;

import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.commons.utils.JwtUtil;
import com.gltqe.wladmin.system.entity.bo.UserDetailsBo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;


/**
 * 自定义过滤器
 * 该过滤器在security核心配置中 被设置为在security拦截之前触发
 * 它是为了验证token的,token合法的话直接在这里认证好security,之后security就不在拦截了.
 */
@Slf4j
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private HandlerExceptionResolver resolver;

    private TokenService tokenService;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        //从请求头部获取token
        String token = request.getHeader(Constant.TOKEN_HEAD);
        TokenService.TokenUserDetails tokenUserDetails = tokenService.getSysUserDetails(token);
        if (!tokenUserDetails.isFlag()) {
            // 抛出异常  // 由于filter中无法抛出异常 不在JwtUtil处理异常 在此处采用HandlerExceptionResolver处理异常抛出
            resolver.resolveException(request, response, null, tokenUserDetails.getRuntimeException());
            // 这里必须return 不然会继续执行
            return;
        }
        UserDetailsBo userDetails = tokenUserDetails.getSysUserDetails();
        Authentication auth = JwtUtil.getAuthentication();
        if (auth == null && userDetails != null) {
            //创建认证信息
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                    userDetails.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
