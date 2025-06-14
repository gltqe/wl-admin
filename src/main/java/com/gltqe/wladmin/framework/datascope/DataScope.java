package com.gltqe.wladmin.framework.datascope;

import java.lang.annotation.*;

/**
 * 数据权限注解
 *
 * @author gltqe
 * @date 2022/7/3 0:58
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    /**
     * 提供部门ID字段的表 别名
     */
    public String dt() default "";

    /**
     * 部门ID字段别名
     */
    String df() default "create_dept";

    /**
     * 提供用户id字段的表 别名
     */
    public String ut() default "";

    /**
     * 用户ID字段别名
     */
    String uf() default "create_id";
}
