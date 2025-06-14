package com.gltqe.wladmin.system.entity.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.gltqe.wladmin.commons.base.BaseVo;
import com.gltqe.wladmin.framework.excel.Dict;
import com.gltqe.wladmin.framework.excel.ExcelDictConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author gltqe
 */
@ExcelIgnoreUnannotated
@Data
@EqualsAndHashCode(callSuper = true)
public class SysPositionVo extends BaseVo {

    /**
     * 职位编码
     **/
    @ExcelProperty("职位编码")
    private String code;

    /**
     * 职位名称
     **/
    @ExcelProperty("职位名称")
    private String name;

    /**
     * 排序
     **/
    @ExcelProperty("排序")
    private Integer sort;

    /**
     * 状态 0 启用 1 停用
     **/
    @ExcelProperty(value = "状态",converter = ExcelDictConverter.class)
    @Dict(dictCode = "sys_status")
    private String status;

    /**
     * 备注
     **/
    @ExcelProperty("备注")
    private String remarks;


}
