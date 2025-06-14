package com.gltqe.wladmin.system.entity.vo;

import com.gltqe.wladmin.commons.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author gltqe
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictItemVo extends BaseVo {
    /**
     * 字典编码
     **/
    private String dictCode;

    /**
     * 字典项文本
     **/
    private String text;

    /**
     * 字典项值
     **/
    private String value;

    /**
     * 状态
     **/
    private String status;

    /**
     * 排序
     **/
    private Integer sort;

    /**
     * 标签样式
     **/
    private String tagType;

    /**
     * 标签主题
     **/
    private String tagEffect;

    /**
     * 备注
     **/
    private String remarks;

}
