package com.gltqe.wladmin.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gltqe.wladmin.system.entity.po.SysDept;
import com.gltqe.wladmin.system.entity.vo.SysDeptVo;
import com.gltqe.wladmin.system.entity.dto.SysDeptDto;

import java.util.List;

/**
 * 部门
 *
 * @author gltqe
 * @date 2022/7/3 1:26
 **/
public interface SysDeptService extends IService<SysDept> {

    /**
     * 获取当前用户可以查看的所有部门
     *
     * @param sysDeptDto
     * @return java.util.List<com.gltqe.wladmin.system.entity.po.SysDept>
     * @author gltqe
     * @date 2022/7/3 1:56
     **/
    List<SysDeptVo> getDeptByUser(SysDeptDto sysDeptDto);

    /**
     * 修改状态
     *
     * @param sysDeptDto
     * @author gltqe
     * @date 2022/7/3 1:26
     **/
    void updateStatus(SysDeptDto sysDeptDto);

    /**
     * 新增
     *
     * @param sysDeptDto
     * @author gltqe
     * @date 2022/7/3 1:26
     **/
    void add(SysDeptDto sysDeptDto);

    /**
     * 修改
     *
     * @param sysDeptDto
     * @author gltqe
     * @date 2022/7/3 1:27
     **/
    void updateDept(SysDeptDto sysDeptDto);

    /**
     * 删除
     *
     * @param ids
     * @author gltqe
     * @date 2022/7/3 1:27
     **/
    void remove(List<String> ids);


}
