package com.gltqe.wladmin.system.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gltqe.wladmin.commons.common.Result;
import com.gltqe.wladmin.system.entity.po.SysConfig;
import com.gltqe.wladmin.system.entity.vo.SysConfigVo;
import com.gltqe.wladmin.system.entity.dto.SysConfigDto;
import com.gltqe.wladmin.system.service.SysConfigService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统配置
 *
 * @author gltqe
 * @date 2022/7/3 1:25
 **/
@RestController
@RequestMapping("/sysConfig")
public class SysConfigController {

    @Resource
    private SysConfigService sysConfigService;

    /**
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:24
     **/
    @RequestMapping("/page")
    @PreAuthorize("@am.hasPermission('config:list:query')")
    public Result page(@RequestBody SysConfigDto sysConfigDto) {
        String name = sysConfigDto.getName();
        String code = sysConfigDto.getCode();
        String type = sysConfigDto.getType();
        String status = sysConfigDto.getStatus();
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(name), SysConfig::getName, name)
                .like(StringUtils.isNotBlank(code), SysConfig::getCode, code)
                .eq(StringUtils.isNotBlank(type), SysConfig::getType, type)
                .eq(StringUtils.isNotBlank(status), SysConfig::getStatus, status);
        Page<SysConfig> sysConfigPage = sysConfigService.page(sysConfigDto.getPage(), wrapper);
        return Result.ok(sysConfigPage);
    }

    /**
     * 修改状态
     *
     * @param sysConfigDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:25
     **/
    @RequestMapping("/updateStatus")
    @PreAuthorize("@am.hasPermission('config:list:update')")
    public Result updateStatus(@RequestBody SysConfigDto sysConfigDto) {
        sysConfigService.updateStatus(sysConfigDto);
        return Result.ok();
    }

    /**
     * 新增
     *
     * @param sysConfigDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:25
     **/
    @RequestMapping("/add")
    @PreAuthorize("@am.hasPermission('config:list:add')")
    public Result add(@RequestBody @Validated SysConfigDto sysConfigDto) {
        sysConfigService.addConfig(sysConfigDto);
        return Result.ok();
    }

    /**
     * 获取详细信息
     *
     * @param sysConfigDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:25
     **/
    @RequestMapping("/getOne")
    @PreAuthorize("@am.hasPermission('config:list:query')")
    public Result getOne(@RequestBody SysConfigDto sysConfigDto) {
        SysConfig sysConfig = sysConfigService.getById(sysConfigDto.getId());
        SysConfigVo sysConfigVo = BeanUtil.copyProperties(sysConfig, SysConfigVo.class);
        return Result.ok(sysConfigVo);
    }

    /**
     * 修改
     *
     * @param sysConfigDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:25
     **/
    @RequestMapping("/update")
    @PreAuthorize("@am.hasPermission('config:list:update')")
    public Result update(@RequestBody @Validated SysConfigDto sysConfigDto) {
        sysConfigService.updateConfig(sysConfigDto);
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
    @PreAuthorize("@am.hasPermission('config:list:remove')")
    public Result remove(@RequestBody List<String> keys) {
        sysConfigService.removeConfig(keys);
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
    @PreAuthorize("@am.hasPermission('config:list:refresh')")
    public Result refresh() {
        sysConfigService.refreshConfig();
        return Result.ok("刷新成功");
    }
}
