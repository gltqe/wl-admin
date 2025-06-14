package com.gltqe.wladmin.framework.interceptor;

import com.gltqe.wladmin.commons.exception.WlException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Slf4j
@Configuration
@Order(1)
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private ApiLimitInterceptor apiLimitInterceptor;
    /**
     * 头像存储路径
     */
    @Value("${upload.path.avatar}")
    private String avatarPath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiLimitInterceptor).addPathPatterns("/**");

    }

    /**
     * 将头像请求映射到目录
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 所有avatar开头的请求会被此资源处理器处理 如果请求 /avatar/aaa/ccc/1.jpg 会映射到 avatarPath路径下 /aaa/ccc/1.jpg
        String path = avatarPath;
        if (avatarPath.startsWith(".") || !Paths.get(avatarPath).isAbsolute()) {
            // 如果是相对路径，转换为绝对路径
            try {
                path = new File(avatarPath).getCanonicalPath();

            } catch (IOException e) {
                throw new WlException("存储路径异常，请联系管理员");
            }
        }
        if (!path.endsWith(File.separator)) {
            path += File.separator;
        }
        log.info("头像存储路径:{}", path);
        registry.addResourceHandler("/avatar/**").addResourceLocations("file:" + path);
    }
}
