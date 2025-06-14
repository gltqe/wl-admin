package com.gltqe.wladmin.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gltqe.wladmin.commons.common.Result;
import com.gltqe.wladmin.system.entity.po.SysApiLimit;
import com.gltqe.wladmin.system.entity.dto.SysApiLimitDto;
import com.gltqe.wladmin.system.service.SysApiLimitService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 接口限制
 *
 * @author gltqe
 * @date 2022/7/3 1:25
 **/
@RestController
@RequestMapping("/sysApiLimit")
public class SysApiLimitController {

    @Resource
    private SysApiLimitService sysApiLimitService;

    /**
     * @param sysApiLimitDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:24
     **/
    @RequestMapping("/page")
    @PreAuthorize("@am.hasPermission('apiLimit:list:query')")
    public Result page(@RequestBody SysApiLimitDto sysApiLimitDto) {
        String uri = sysApiLimitDto.getUri();
        String name = sysApiLimitDto.getName();
        String status = sysApiLimitDto.getStatus();
        LambdaQueryWrapper<SysApiLimit> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(name), SysApiLimit::getName, name)
                .like(StringUtils.isNotBlank(uri), SysApiLimit::getUri, uri)
                .eq(status != null, SysApiLimit::getStatus, status);
        Page<SysApiLimit> page = sysApiLimitDto.getPage();
        Page<SysApiLimit> sysApiLimitPage = sysApiLimitService.page(page, wrapper);
        return Result.ok(sysApiLimitPage);
    }

    /**
     * 修改状态
     *
     * @param sysApiLimitDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:25
     **/
    @RequestMapping("/updateStatus")
    @PreAuthorize("@am.hasPermission('apiLimit:list:update')")
    public Result updateStatus(@RequestBody SysApiLimitDto sysApiLimitDto) {
        sysApiLimitService.updateStatus(sysApiLimitDto);
        return Result.ok();
    }

    /**
     * 新增
     *
     * @param sysApiLimitDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:25
     **/
    @RequestMapping("/add")
    @PreAuthorize("@am.hasPermission('apiLimit:list:add')")
    public Result add(@RequestBody SysApiLimitDto sysApiLimitDto) {
        sysApiLimitService.addApiLimit(sysApiLimitDto);
        return Result.ok();
    }

    /**
     * 获取详细信息
     *
     * @param sysApiLimitDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:25
     **/
    @RequestMapping("/getOne")
    @PreAuthorize("@am.hasPermission('apiLimit:list:query')")
    public Result getOne(@RequestBody SysApiLimitDto sysApiLimitDto) {
        SysApiLimit byId = sysApiLimitService.getById(sysApiLimitDto.getId());
        return Result.ok(byId);
    }

    /**
     * 修改
     *
     * @param sysApiLimitDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:25
     **/
    @RequestMapping("/update")
    @PreAuthorize("@am.hasPermission('apiLimit:list:update')")
    public Result update(@RequestBody SysApiLimitDto sysApiLimitDto) {
        sysApiLimitService.updateApiLimit(sysApiLimitDto);
        return Result.ok();
    }

    /**
     * 删除
     *
     * @param keys
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:25
     **/
    @RequestMapping("/remove")
    @PreAuthorize("@am.hasPermission('apiLimit:list:remove')")
    public Result remove(@RequestBody List<String> keys) {
        sysApiLimitService.removeApiLimitByUri(keys);
        return Result.ok("删除成功");
    }

    /**
     * 刷新redis
     *
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:25
     **/
    @RequestMapping("/refresh")
    @PreAuthorize("@am.hasPermission('apiLimit:list:refresh')")
    public Result refresh() {
        sysApiLimitService.refreshApiLimit();
        return Result.ok("刷新成功");
    }
}
