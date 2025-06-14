package com.gltqe.wladmin.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gltqe.wladmin.commons.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author gltqe
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_item")
public class SysDictItem extends BaseEntity {
    /**
     * 字典编码
     **/
//    @NotBlank(message = "字典编码不能为空")
//    @Size(min = 1,max = 60,message = "字典编码不能超过60个字符")
    @TableField("dict_code")
    private String dictCode;

    /**
     * 字典项文本
     **/
//    @NotBlank(message = "字典项文本不能为空")
//    @Size(min = 1,max = 60,message = "字典项文本不能超过60个字符")
    @TableField("text")
    private String text;

    /**
     * 字典项值
     **/
//    @NotBlank(message = "字典项值不能为空")
//    @Size(min = 1,max = 60,message = "字典项值不能超过60个字符")
    @TableField("value")
    private String value;

    /**
     * 状态
     **/
    @TableField("status")
    private String status;

    /**
     * 排序
     **/
    @TableField("sort")
    private Integer sort;

    /**
     * 标签样式
     **/
    @TableField("tag_type")
    private String tagType;

    /**
     * 标签主题
     **/
    @TableField("tag_effect")
    private String tagEffect;

    /**
     * 备注
     **/
    @TableField("remarks")
    private String remarks;

}
