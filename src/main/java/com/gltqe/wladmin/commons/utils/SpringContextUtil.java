package com.gltqe.wladmin.commons.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * spring工具类
 *
 * @author gltqe
 * @date 2022/7/3 0:31
 **/
@Component("springContextUtil")
public class SpringContextUtil implements ApplicationContextAware {
    public static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * 获取bean
     *
     * @param name
     * @return java.lang.Object
     * @author
     * @date 2022/7/3 0:31
     **/
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }
    /**
     * 通过class获取Bean.
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    /**
     * 获取bean
     *
     * @param name
     * @param requiredType
     * @return T
     * @author
     * @date 2022/7/3 0:31
     **/
    public static <T> T getBean(String name, Class<T> requiredType) {
        return applicationContext.getBean(name, requiredType);
    }

    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    public static boolean isSingleton(String name) {
        return applicationContext.isSingleton(name);
    }

    public static Class<? extends Object> getType(String name) {
        return applicationContext.getType(name);
    }

    /**
     * 获取 HttpServletRequest
     *
     * @return jakarta.servlet.http.HttpServletRequest
     * @author gltqe
     * @date 2022/7/3 0:31
     **/
    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    /**
     * 获取 HttpServletResponse
     *
     * @return jakarta.servlet.http.HttpServletResponse
     * @author gltqe
     * @date 2022/7/3 0:31
     **/
    public static HttpServletResponse getHttpServletResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
    }
}
