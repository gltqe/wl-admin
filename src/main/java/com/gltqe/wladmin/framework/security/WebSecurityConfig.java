package com.gltqe.wladmin.framework.security;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * SpringSecurity核心配置
 *
 * @author gltqe
 * @date 2022/7/3 1:07
 **/
@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    @Resource
    private AuthenticationConfiguration authenticationConfiguration;

    @Resource
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Resource
    private TokenService tokenService;

    /**
     *
     *
     * @return WebSecurityCustomizer
     * @author gltqe
     * @date 2023/5/17 14:23
     **/
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return new WebSecurityCustomizer() {
            @Override
            public void customize(WebSecurity web) {
                // 放行登录接口
                web.ignoring().requestMatchers("/login", "/getCaptcha","/refreshToken","/avatar/**");
            }
        };
    }
    /**
     * SpringSecurity过滤器
     * @param http
     * @return SecurityFilterChain
     * @author gltqe
     * @date 2023/5/17 14:25
     **/
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.sessionManagement(new Customizer<SessionManagementConfigurer<HttpSecurity>>() {
                    @Override
                    public void customize(SessionManagementConfigurer<HttpSecurity> httpSecuritySessionManagementConfigurer) {
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    }
                })
                // 添加自定义过滤器
                // 这里如果采用注入的方式(自定义过滤器作为bean交给spring管理了) , WebSecurity或HttpSecurity放开的接口仍然会走自定义过滤器,必须通过new的方式
                // 参考文章 https://blog.csdn.net/wh2574021892/article/details/123479176
                .addFilterBefore(new JwtAuthenticationFilter(resolver,tokenService), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(new Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>() {
                    @Override
                    public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizationManagerRequestMatcherRegistry) {
                        authorizationManagerRequestMatcherRegistry.requestMatchers(HttpMethod.OPTIONS).permitAll();
                        authorizationManagerRequestMatcherRegistry.anyRequest().authenticated();
                    }
                })
                .logout(new Customizer<LogoutConfigurer<HttpSecurity>>() {
                    @Override
                    public void customize(LogoutConfigurer<HttpSecurity> httpSecurityLogoutConfigurer) {
                        httpSecurityLogoutConfigurer.logoutUrl("/logout");
                        httpSecurityLogoutConfigurer.logoutSuccessHandler(new CustomLogoutSuccessHandler());

                    }
                })
                .csrf(new Customizer<CsrfConfigurer<HttpSecurity>>() {
                    @Override
                    public void customize(CsrfConfigurer<HttpSecurity> httpSecurityCsrfConfigurer) {
                        httpSecurityCsrfConfigurer.disable();
                    }
                }).build();
    }



    /**
     * SpringSecurity 提供的加密工具，可快速实现加密加盐
     *
     * @return org.springframework.security.crypto.password.PasswordEncoder
     * @author gltqe
     * @date 2022/7/3 1:08
     **/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 认证接口
     *
     * @return org.springframework.security.authentication.AuthenticationManager
     * @author gltqe
     * @date 2022/7/3 1:22
     **/
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
        return authenticationManager;
    }
}
