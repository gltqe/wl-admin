package com.gltqe.wladmin.commons.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 响应参数基类
 *
 * @author gltqe
 * @date 2023/5/14 14:30
 **/
@Data
public class BaseVo {

    private String id;

    /**
     * 是否删除 0 未删除 1 已删除
     **/
    private Integer isDelete;

    /**
     * 创建人ID
     **/
    private String createId;

    /**
     * 创建时间
     **/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 最后修改人ID
     **/
    private String updateId;

    /**
     * 最后修改时间
     **/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
