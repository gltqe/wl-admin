package com.gltqe.wladmin.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gltqe.wladmin.system.entity.po.SysDictItem;
import com.gltqe.wladmin.system.entity.dto.SysDictItemDto;

import java.util.List;

/**
 * 字典项
 *
 * @author gltqe
 * @date 2022/7/3 1:28
 **/
public interface SysDictItemService extends IService<SysDictItem> {
    /**
     * 修改状态
     *
     * @param sysDictItemDto
     * @author gltqe
     * @date 2022/7/3 1:28
     **/
    void updateStatus(SysDictItemDto sysDictItemDto);

    /**
     * 新增
     *
     * @param sysDictItemDto
     * @author gltqe
     * @date 2022/7/3 1:28
     **/
    void addDictItem(SysDictItemDto sysDictItemDto);

    /**
     * 修改
     *
     * @param sysDictItemDto
     * @author gltqe
     * @date 2022/7/3 1:28
     **/
    void updateDictItem(SysDictItemDto sysDictItemDto);

    /**
     * 删除
     *
     * @param ids
     * @author gltqe
     * @date 2022/7/3 1:29
     **/
    void removeDictItem(List<String> ids);

}
