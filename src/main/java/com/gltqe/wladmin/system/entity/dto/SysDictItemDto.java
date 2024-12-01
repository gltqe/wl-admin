package com.gltqe.wladmin.system.entity.dto;

import com.gltqe.wladmin.commons.base.BaseDto;
import com.gltqe.wladmin.system.entity.po.SysDictItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author gltqe
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictItemDto extends BaseDto<SysDictItem> {
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
