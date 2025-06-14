package com.gltqe.wladmin.system.controller;

import cn.hutool.core.bean.BeanUtil;
import com.gltqe.wladmin.commons.common.Result;
import com.gltqe.wladmin.system.entity.po.SysDept;
import com.gltqe.wladmin.system.entity.vo.SysDeptVo;
import com.gltqe.wladmin.system.entity.dto.SysDeptDto;
import com.gltqe.wladmin.system.service.SysDeptService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 部门
 *
 * @author gltqe
 * @date 2022/7/3 1:26
 **/
@RestController
@RequestMapping("/sysDept")
public class SysDeptController {
    @Resource
    private SysDeptService sysDeptService;

    /**
     * 获取当前用户可以查看的所有部门
     *
     * @param sysDeptDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:26
     **/
    @RequestMapping("/getDeptByUser")
    @PreAuthorize("@am.hasPermission('dept:list:query')")
    public Result getDeptByUser(@RequestBody SysDeptDto sysDeptDto) {
        List<SysDeptVo> list = sysDeptService.getDeptByUser(sysDeptDto);
        return Result.ok(list);
    }

    /**
     * 修改状态
     *
     * @param sysDeptDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:26
     **/
    @RequestMapping("/updateStatus")
    @PreAuthorize("@am.hasPermission('dept:list:update')")
    public Result updateStatus(@RequestBody SysDeptDto sysDeptDto) {
        sysDeptService.updateStatus(sysDeptDto);
        return Result.ok();
    }

    /**
     * 新增
     *
     * @param sysDeptDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:26
     **/
    @RequestMapping("/add")
    @PreAuthorize("@am.hasPermission('dept:list:add')")
    public Result add(@RequestBody SysDeptDto sysDeptDto) {
        sysDeptService.add(sysDeptDto);
        return Result.ok("新增成功");
    }

    /**
     * 获取详细信息
     *
     * @param sysDeptDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:26
     **/
    @RequestMapping("/getOne")
    @PreAuthorize("@am.hasPermission('dept:list:query')")
    public Result getOne(@RequestBody SysDeptDto sysDeptDto) {
        SysDept dept = sysDeptService.getById(sysDeptDto.getId());
        SysDeptVo sysDeptVo = BeanUtil.copyProperties(dept, SysDeptVo.class);
        return Result.ok(sysDeptVo);
    }

    /**
     * 修改
     *
     * @param sysDeptDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:27
     **/
    @RequestMapping("/update")
    @PreAuthorize("@am.hasPermission('dept:list:update')")
    public Result update(@RequestBody SysDeptDto sysDeptDto) {
        sysDeptService.updateDept(sysDeptDto);
        return Result.ok();
    }

    /**
     * 删除
     *
     * @param ids
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:27
     **/
    @RequestMapping("/remove")
    @PreAuthorize("@am.hasPermission('dept:list:remove')")
    public Result remove(@RequestBody List<String> ids) {
        sysDeptService.remove(ids);
        return Result.ok("删除成功");
    }
}
