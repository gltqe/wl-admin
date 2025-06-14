package com.gltqe.wladmin.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gltqe.wladmin.system.entity.po.SysApiLimit;
import com.gltqe.wladmin.system.entity.dto.SysApiLimitDto;

import java.util.List;

/**
 * 接口
 *
 * @author gltqe
 * @date 2022/7/5 9:43
 **/
public interface SysApiLimitService extends IService<SysApiLimit> {
    /**
     * 加载核心配置到内存
     *
     * @author gltqe
     * @date 2023/5/19 10:10
     **/
    void loadApiLimit();

    /**
     * 修改状态
     *
     * @param sysApiLimitDto
     * @author gltqe
     * @date 2022/7/3 1:25
     **/
    void updateStatus(SysApiLimitDto sysApiLimitDto);

    /**
     * 新增
     *
     * @param sysApiLimitDto
     * @author gltqe
     * @date 2022/7/3 1:25
     **/
    void addApiLimit(SysApiLimitDto sysApiLimitDto);

    /**
     * 修改
     *
     * @param sysApiLimitDto
     * @author gltqe
     * @date 2022/7/3 1:25
     **/
    void updateApiLimit(SysApiLimitDto sysApiLimitDto);

    /**
     * 删除
     *
     * @param keys
     * @author gltqe
     * @date 2022/7/3 1:25
     **/
    void removeApiLimitByUri(List<String> keys);

    /**
     * 刷新redis
     *
     * @author gltqe
     * @date 2022/7/3 1:25
     **/
    void refreshApiLimit();
}
