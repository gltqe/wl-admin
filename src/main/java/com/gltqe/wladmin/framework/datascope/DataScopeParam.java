package com.gltqe.wladmin.framework.datascope;

import lombok.Data;

/**
 * 数据权限实体类
 *
 * @author gltqe
 * @date 2022/7/3 1:02
 **/
@Data
public class DataScopeParam {

    /**
     * 部门表别名
     */
    private String dt;

    /**
     * 部门ID字段别名
     */
    private String df;

    /**
     * 用户表别名
     */
    private String ut;

    /**
     * 用户ID字段别名
     */
    private String uf;

}
