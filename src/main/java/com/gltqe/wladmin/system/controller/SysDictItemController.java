package com.gltqe.wladmin.system.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gltqe.wladmin.commons.common.Result;
import com.gltqe.wladmin.system.entity.po.SysDictItem;
import com.gltqe.wladmin.system.entity.vo.SysDictItemVo;
import com.gltqe.wladmin.system.entity.dto.SysDictItemDto;
import com.gltqe.wladmin.system.service.SysDictItemService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 字典项
 *
 * @author gltqe
 * @date 2022/7/3 1:28
 **/
@RestController
@RequestMapping("/sysDictItem")
public class SysDictItemController {

    @Resource
    private SysDictItemService sysDictItemService;

    /**
     * 分页查询
     *
     * @param sysDictItemDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:28
     **/
    @RequestMapping("/page")
    @PreAuthorize("@am.hasPermission('dictItem:list:query')")
    public Result page(@RequestBody SysDictItemDto sysDictItemDto) {
        String text = sysDictItemDto.getText();
        String value = sysDictItemDto.getValue();
        String status = sysDictItemDto.getStatus();
        LambdaQueryWrapper<SysDictItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(text), SysDictItem::getText, text)
                .like(StringUtils.isNotBlank(value), SysDictItem::getValue, value)
                .eq(StringUtils.isNotBlank(status), SysDictItem::getStatus, status)
                .eq(SysDictItem::getDictCode, sysDictItemDto.getDictCode())
                .orderByAsc(SysDictItem::getSort);
        Page<SysDictItem> sysDictItemPage = sysDictItemService.page(sysDictItemDto.getPage(), wrapper);
        return Result.ok(sysDictItemPage);
    }

    /**
     * 修改状态
     *
     * @param sysDictItemDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:28
     **/
    @RequestMapping("/updateStatus")
    @PreAuthorize("@am.hasPermission('dictItem:list:update')")
    public Result updateStatus(@RequestBody SysDictItemDto sysDictItemDto) {
        sysDictItemService.updateStatus(sysDictItemDto);
        return Result.ok();
    }

    /**
     * 新增
     *
     * @param sysDictItemDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:28
     **/
    @RequestMapping("/add")
    @PreAuthorize("@am.hasPermission('dictItem:list:add')")
    public Result add(@RequestBody SysDictItemDto sysDictItemDto) {
        sysDictItemService.addDictItem(sysDictItemDto);
        return Result.ok();
    }

    /**
     * 获取详细信息
     *
     * @param sysDictItemDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:28
     **/
    @RequestMapping("/getOne")
    @PreAuthorize("@am.hasPermission('dictItem:list:query')")
    public Result getOne(@RequestBody SysDictItemDto sysDictItemDto) {
        SysDictItem sysDictItem= sysDictItemService.getById(sysDictItemDto.getId());
        SysDictItemVo sysDictItemVo = BeanUtil.copyProperties(sysDictItem, SysDictItemVo.class);
        return Result.ok(sysDictItemVo);
    }

    /**
     * 修改
     *
     * @param sysDictItemDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:28
     **/
    @RequestMapping("/update")
    @PreAuthorize("@am.hasPermission('dictItem:list:update')")
    public Result update(@RequestBody SysDictItemDto sysDictItemDto) {
        sysDictItemService.updateDictItem(sysDictItemDto);
        return Result.ok();
    }

    /**
     * 删除
     *
     * @param ids
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:29
     **/
    @RequestMapping("/remove")
    @PreAuthorize("@am.hasPermission('dictItem:list:remove')")
    public Result remove(@RequestBody List<String> ids) {
        sysDictItemService.removeDictItem(ids);
        return Result.ok("删除成功");
    }

}
