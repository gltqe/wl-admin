package com.gltqe.wladmin.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gltqe.wladmin.system.entity.po.SysDict;
import com.gltqe.wladmin.system.entity.dto.SysDictDto;

import java.util.List;

/**
 * 字典
 *
 * @author gltqe
 * @date 2022/7/3 1:27
 **/
public interface SysDictService extends IService<SysDict> {
    /**
     * 加载字典到redis
     *
     * @author gltqe
     * @date 2023/5/19 10:09
     **/
    void loadDict();

    /**
     * 修改状态
     *
     * @param sysDictDto
     * @author gltqe
     * @date 2022/7/3 1:27
     **/
    void updateStatus(SysDictDto sysDictDto);

    /**
     * 新增
     *
     * @param sysDictDto
     * @author gltqe
     * @date 2022/7/3 1:27
     **/
    void addDict(SysDictDto sysDictDto);

    /**
     * 修改
     *
     * @param sysDictDto
     * @author gltqe
     * @date 2022/7/3 1:28
     **/
    void updateDict(SysDictDto sysDictDto);

    /**
     * 删除
     *
     * @param ids
     * @author gltqe
     * @date 2022/7/3 1:28
     **/
    void removeDict(List<String> ids);

    /**
     * 刷新redis
     *
     * @author gltqe
     * @date 2022/7/3 1:54
     **/
    void refreshDict();
}
