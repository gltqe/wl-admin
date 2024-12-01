package com.gltqe.wladmin.framework.log;


import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSONObject;
import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.commons.enums.OperationLogTypeEnum;
import com.gltqe.wladmin.commons.utils.IpUtil;
import com.gltqe.wladmin.commons.utils.JwtUtil;
import com.gltqe.wladmin.commons.utils.SpringContextUtil;
import com.gltqe.wladmin.monitor.service.OperationLogService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 日志aop
 *
 * @author gltqe
 * @date 2022/7/3 0:45
 **/
@Slf4j
@Aspect
@Component
public class LogAspect {

    private static final Integer SAVE_LENGTH = 1000;

    ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    @Resource
    private OperationLogService operationLogService;

    /**
     * 配置织入点
     *
     * @author gltqe
     * @date 2022/7/3 0:45
     **/
    @Pointcut("@annotation(com.gltqe.wladmin.framework.log.Log)")
    public void logPointCut() {
    }

    /**
     * 前置通知 记录开始时间戳
     *
     * @param point
     * @author gltqe
     * @date 2022/7/3 0:45
     **/
    @Before("logPointCut()")
    public void logBefore(JoinPoint point) {
        // 设置开始时间戳
        threadLocal.set(System.currentTimeMillis());
    }

    /**
     * 抛出异常后通知 在方法抛出异常退出时执行的通知
     *
     * @param point
     * @param e
     * @author gltqe
     * @date 2022/7/3 0:46
     **/
    @AfterThrowing(value = "@annotation(com.gltqe.wladmin.framework.log.Log)", throwing = "e")
    public void logThrowing(JoinPoint point, Throwable e) {
        saveLog(point, e, null);
    }

    /**
     * 在某连接点正常完成后执行的通知，不包括抛出异常的情况
     *
     * @param point
     * @param result
     * @author gltqe
     * @date 2022/7/3 0:46
     **/
    @AfterReturning(value = "logPointCut()", returning = "result")
    public void logAfterReturning(JoinPoint point, Object result) {
        saveLog(point, null, result);
    }

    /**
     * 当某连接点退出的时候执行的通知（不论是正常返回还是异常退出）
     *
     * @param point
     * @author gltqe
     * @date 2022/7/3 0:46
     **/
    @After("logPointCut()")
    public void logAfter(JoinPoint point) {
        threadLocal.remove();
    }

    public void saveLog(JoinPoint point, Throwable throwable, Object result) {
        Long start = threadLocal.get();
        OperationLogEvent logEvent = new OperationLogEvent();
        // 保存注解参数
        saveLogParams(point, logEvent, result);
        // 设置开始时间
        logEvent.setRequestTime(new Date(start));
        // 获取请求方式
        HttpServletRequest request = SpringContextUtil.getHttpServletRequest();
        String method = request.getMethod();
        logEvent.setRequestType(method);
        // 获取请求uri
        String requestURI = request.getRequestURI();
        logEvent.setRequestUri(requestURI);
        // 获取请求IP
        logEvent.setRequestIp(IpUtil.getIpAddress(request));
        // 判断响应结果
        if (throwable != null) {
            logEvent.setStatus(Constant.Y);
            String message = throwable.getMessage();
            if (message.length() > SAVE_LENGTH) {
                message = message.substring(0, SAVE_LENGTH);
            }
            logEvent.setErrorMsg(message);
        } else {
            logEvent.setStatus(Constant.N);
        }
        // 执行人
        String username = JwtUtil.getUsername();
        logEvent.setOperator(username);
        // 部门
        String deptId = JwtUtil.getDeptId();
        logEvent.setDeptId(deptId);
        // 响应时间
        logEvent.setResponseTime(new Date());
        long l = System.currentTimeMillis() - start;
        logEvent.setExecuteTime(String.valueOf(l));
        SpringUtil.getApplicationContext().publishEvent(logEvent);
    }

    public void saveLogParams(JoinPoint point, OperationLogEvent logEvent, Object result) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        // 获取方法名称
        String declaringTypeName = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        logEvent.setMethodName(declaringTypeName + "." + methodName);
        // 获取注解
        Log annotation = signature.getMethod().getAnnotation(Log.class);
        if (annotation != null) {
            // 获取注解参数
            String name = annotation.name();
            OperationLogTypeEnum type = annotation.type();
            boolean recordParams = annotation.recordParams();
            boolean recordResult = annotation.recordResult();
            logEvent.setName(name);
            logEvent.setType(type.getCode());
            // 保存请求参数
            if (recordParams) {
                // 参数名称
                //  String[] parameterNames = signature.getParameterNames();
                // 参数值
                Object[] args = point.getArgs();
                String params = JSONObject.toJSONString(args);
                if (params.length() > SAVE_LENGTH) {
                    params = params.substring(0, SAVE_LENGTH);
                }
                logEvent.setRequestParams(params);
            }
            // 保存响应参数
            if (recordResult) {
                if (result != null) {
                    String r = JSONObject.toJSONString(result);
                    if (r.length() > SAVE_LENGTH) {
                        r = r.substring(0, SAVE_LENGTH);
                    }
                    logEvent.setResponseResult(r);
                }
            }
        }
    }
}
