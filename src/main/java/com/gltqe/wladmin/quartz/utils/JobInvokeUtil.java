package com.gltqe.wladmin.quartz.utils;


import com.gltqe.wladmin.commons.utils.SpringContextUtil;
import com.gltqe.wladmin.quartz.entity.po.ScheduleJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * 任务执行工具
 *
 * @author ruoyi
 */
@Slf4j
public class JobInvokeUtil {

    public static void invokeMethod(ScheduleJob job) throws Exception {
        invokeMethod(job.getBeanName(), job.getMethodName(), job.getParams());
    }


    public static void invokeMethod(String beanName, String methodName, String params) throws Exception {
        Object bean = null;
        if (!isValidClassName(beanName)) {
            bean = SpringContextUtil.getBean(beanName);
        } else {
            bean = Class.forName(beanName).newInstance();
        }

        if (StringUtils.isNotBlank(params)) {
            Method method = bean.getClass().getDeclaredMethod(methodName, String.class);
            method.invoke(bean, params);
        } else {
            Method method = bean.getClass().getDeclaredMethod(methodName);
            method.invoke(bean);
        }

    }

    /**
     * 校验是否为为class包名
     *
     * @param invokeTarget 名称
     * @return true是 false否
     */
    public static boolean isValidClassName(String invokeTarget) {
        return StringUtils.countMatches(invokeTarget, ".") > 1;
    }
}
