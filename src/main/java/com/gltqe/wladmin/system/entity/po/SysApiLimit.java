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
@TableName("sys_api_limit")
public class SysApiLimit extends BaseEntity {

    /**
     * 接口名称
     **/
//    @NotBlank(message = "接口名称不能为空")
//    @Size(min = 1,max = 60,message = "接口名称不能超过60个字符")
    @TableField("name")
    private String name;

    /**
     * 接口uri
     **/
//    @NotBlank(message = "接口uri不能为空")
//    @Size(min = 1,max = 60,message = "接口uri不能超过120个字符")
    @TableField("uri")
    private String uri;

    /**
     * 单个用户次数
     **/
    @TableField("single_frequency")
    private Long singleFrequency;
    /**
     * 单个用户时间
     **/
    @TableField("single_time")
    private String singleTime;
    /**
     * 单个用户时间单位
     **/
    @TableField("single_time_unit")
    private String singleTimeUnit;

    /**
     * 单个用户时间(秒)
     **/
    @TableField("single_time_second")
    private Long singleTimeSecond;

    /**
     * 单个用户令牌创建速度
     **/
    @TableField("single_limiter_rate")
    private String singleLimiterRate;
    /**
     * 全部用户次数
     **/
    @TableField("whole_frequency")
    private Long wholeFrequency;
    /**
     * 全部用户时间
     **/
    @TableField("whole_time")
    private String wholeTime;
    /**
     * 全部用户时间单位
     **/
    @TableField("whole_time_unit")
    private String wholeTimeUnit;

    /**
     * 全部用户时间(秒)
     **/
    @TableField("whole_time_second")
    private Long wholeTimeSecond;

    /**
     * 全部用户令牌创建速度
     **/
    @TableField("whole_limiter_rate")
    private String wholeLimiterRate;

    /**
     * 状态 0 启用 1 停用
     **/
    @TableField("status")
    private String status;

    /**
     * 备注
     **/
    @TableField("remarks")
    private String remarks;




}
