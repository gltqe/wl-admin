package com.gltqe.wladmin.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gltqe.wladmin.system.entity.po.SysConfig;
import com.gltqe.wladmin.system.entity.dto.SysConfigDto;

import java.util.List;

/**
 * 系统配置
 *
 * @author gltqe
 * @date 2022/7/3 1:25
 **/
public interface SysConfigService extends IService<SysConfig> {


    /**
     * 加载核心配置到redis
     *
     * @author gltqe
     * @date 2023/5/19 10:06
     **/
    public void loadConfig();

    /**
     * 修改状态
     *
     * @param sysConfigDto
     * @author gltqe
     * @date 2022/7/3 1:25
     **/
    void updateStatus(SysConfigDto sysConfigDto);

    /**
     * 新增
     *
     * @param sysConfigDto
     * @author gltqe
     * @date 2022/7/3 1:25
     **/
    void addConfig(SysConfigDto sysConfigDto);


    /**
     * 修改
     *
     * @param sysConfigDto
     * @author gltqe
     * @date 2022/7/3 1:25
     **/
    void updateConfig(SysConfigDto sysConfigDto);

    /**
     * 删除
     *
     * @param keys
     * @author gltqe
     * @date 2022/7/3 1:25
     **/
    void removeConfig(List<String> keys);

    /**
     * 刷新redis
     *
     * @author gltqe
     * @date 2022/7/3 1:25
     **/
    void refreshConfig();

}
