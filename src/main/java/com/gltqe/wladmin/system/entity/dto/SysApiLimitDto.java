package com.gltqe.wladmin.system.entity.dto;

import com.gltqe.wladmin.commons.base.BaseDto;
import com.gltqe.wladmin.system.entity.po.SysApiLimit;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 *
 * @author: gltqe
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysApiLimitDto extends BaseDto<SysApiLimit> {

    /**
     * 接口名称
     **/
    private String name;

    /**
     * 接口uri
     **/
    private String uri;

    /**
     * 单个用户次数
     **/
    private Long singleFrequency;

    /**
     * 单个用户时间
     **/
    private String singleTime;

    /**
     * 单个用户时间单位
     **/
    private String singleTimeUnit;

    /**
     * 单个用户时间(秒)
     **/
    private Long singleTimeSecond;

    /**
     * 单个用户令牌创建速度
     **/
    private String singleLimiterRate;

    /**
     * 全部用户次数
     **/
    private Long wholeFrequency;

    /**
     * 全部用户时间
     **/
    private String wholeTime;

    /**
     * 全部用户时间单位
     **/
    private String wholeTimeUnit;

    /**
     * 全部用户时间(秒)
     **/
    private Long wholeTimeSecond;

    /**
     * 全部用户令牌创建速度
     **/
    private String wholeLimiterRate;

    /**
     * 状态 0 启用 1 停用
     **/
    private String status;

    /**
     * 备注
     **/
    private String remarks;

}
