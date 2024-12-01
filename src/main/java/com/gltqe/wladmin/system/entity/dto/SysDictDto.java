package com.gltqe.wladmin.system.entity.dto;

import com.gltqe.wladmin.commons.base.BaseDto;
import com.gltqe.wladmin.system.entity.po.SysDict;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author gltqe
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictDto extends BaseDto<SysDict> {
    /**
     * 字典名称
     **/
    private String name;

    /**
     * 字典编码
     **/
    private String code;

    /**
     * 状态
     **/
    private String status;

    /**
     * 获取类型 0 list 1 map 2 list和map
     **/
    private Integer type;


}
