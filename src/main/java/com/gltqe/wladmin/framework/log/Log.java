package com.gltqe.wladmin.framework.log;

import com.gltqe.wladmin.commons.enums.OperationLogTypeEnum;

import java.lang.annotation.*;

/**
 * 日志注解
 *
 * @author gltqe
 * @date 2022/7/3 0:45
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * 标题
     **/
    public String name() default "";

    /**
     * 操作类型
     **/
    public OperationLogTypeEnum type() default OperationLogTypeEnum.OTHER;

    /**
     * 是否记录请求参数
     **/
    public boolean recordParams() default false;

    /**
     * 是否记录响应结果
     **/
    public boolean recordResult() default false;
}
