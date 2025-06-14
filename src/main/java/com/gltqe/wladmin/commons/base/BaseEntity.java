package com.gltqe.wladmin.commons.base;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.util.Date;

/**
 * 实体类基类
 *
 * @author gltqe
 * @date 2023/5/14 14:30
 **/
@Data
public class BaseEntity extends Model {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 是否删除 0 未删除 1 已删除
     **/
    @TableField("is_delete")
    @TableLogic(value = "0", delval = "1")
    private Integer isDelete;

    /**
     * 创建人ID
     **/
    @TableField(fill = FieldFill.INSERT)
    private String createId;

    /**
     * 创建时间
     **/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 最后修改人ID
     **/
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateId;

    /**
     * 最后修改时间
     **/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
