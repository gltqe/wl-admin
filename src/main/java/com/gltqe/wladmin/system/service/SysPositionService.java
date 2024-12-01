package com.gltqe.wladmin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gltqe.wladmin.system.entity.po.SysPosition;
import com.gltqe.wladmin.system.entity.dto.SysPositionDto;

import java.util.List;

/**
 * 职位
 *
 * @author gltqe
 * @date 2022/7/3 2:03
 **/
public interface SysPositionService extends IService<SysPosition> {
    /**
     * 职位列表
     *
     * @param sysPositionDto
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.gltqe.wladmin.system.entity.po.SysPosition>
     * @author gltqe
     * @date 2022/7/3 2:03
     **/
    IPage<SysPosition> page(SysPositionDto sysPositionDto);

    /**
     * 修改状态
     *
     * @param sysPositionDto
     * @author gltqe
     * @date 2022/7/3 2:04
     **/
    void updateStatus(SysPositionDto sysPositionDto);

    /**
     * 新增
     *
     * @param sysPositionDto
     * @author gltqe
     * @date 2022/7/3 2:04
     **/
    void addPosition(SysPositionDto sysPositionDto);


    /**
     * 修改
     *
     * @param sysPositionDto
     * @author gltqe
     * @date 2022/7/3 2:04
     **/
    void updatePosition(SysPositionDto sysPositionDto);

    /**
     * 删除
     *
     * @param ids
     * @author gltqe
     * @date 2022/7/3 2:04
     **/
    void removePosition(List<String> ids);
}
