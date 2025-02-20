package com.gltqe.wladmin.framework.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字典自定义注解
 *
 * @author gltqe
 * @date 2023/6/14 14:59
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Dict {
    /**
     * 字典编码
     */
    String dictCode() default "";

    /**
     * 字典转换式  例如:  1=男;2=女
     */
    String dictExp() default "";

    /**
     * 字典分割
     */
    String sepDict() default ";";

    /**
     * 键值对分割
     */
    String sepPair() default "=";
}
