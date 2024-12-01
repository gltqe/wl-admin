package com.gltqe.wladmin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gltqe.wladmin.commons.common.Result;
import com.gltqe.wladmin.system.entity.po.SysRole;
import com.gltqe.wladmin.system.entity.vo.SysRoleVo;
import com.gltqe.wladmin.system.entity.dto.SysRoleDto;
import com.gltqe.wladmin.system.service.SysRoleService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * 角色
 * @author gltqe
 * @date 2022/7/3 2:06
 **/
@RestController
@RequestMapping("/sysRole")
public class SysRoleController {
    @Resource
    private SysRoleService sysRoleService;

    /**
     * 分页查询 只能查看当前用户拥有的角色
     *
     * @param sysRoleDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:34
     **/
    @RequestMapping("/page")
    @PreAuthorize("@am.hasPermission('role:list:query')")
    public Result getRolePageByUser(@RequestBody SysRoleDto sysRoleDto) {
        IPage<SysRoleVo> iPage = sysRoleService.page(sysRoleDto);
        return Result.ok(iPage);
    }

    /**
     * 当前用户可分配角色
     *
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:34
     **/
    @RequestMapping("/getRoleByUser")
    @PreAuthorize("@am.hasPermission('role:list:query')")
    public Result getRoleByUser() {
        List<SysRole> list = sysRoleService.getRoleByUser();
        return Result.ok(list);
    }

    /**
     * 修改状态
     *
     * @param sysRoleDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:34
     **/
    @RequestMapping("/updateStatus")
    @PreAuthorize("@am.hasPermission('role:list:update')")
    public Result updateStatus(@RequestBody SysRoleDto sysRoleDto) {
        sysRoleService.updateStatus(sysRoleDto);
        return Result.ok();
    }

    /**
     * 新增
     *
     * @param sysRoleDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:34
     **/
    @RequestMapping("/add")
    @PreAuthorize("@am.hasPermission('role:list:add')")
    public Result add(@RequestBody SysRoleDto sysRoleDto) {
        sysRoleService.addRole(sysRoleDto);
        return Result.ok();
    }

    /**
     * 获取详细信息
     *
     * @param sysRoleDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:34
     **/
    @RequestMapping("/getOne")
    @PreAuthorize("@am.hasPermission('role:list:query')")
    public Result getOne(@RequestBody SysRoleDto sysRoleDto) {
        SysRoleVo byId = sysRoleService.getOne(sysRoleDto);
        return Result.ok(byId);
    }

    /**
     * 修改
     *
     * @param sysRoleDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:35
     **/
    @RequestMapping("/update")
    @PreAuthorize("@am.hasPermission('role:list:update')")
    public Result update(@RequestBody SysRoleDto sysRoleDto) {
        sysRoleService.updateRole(sysRoleDto);
        return Result.ok();
    }

    /**
     * 删除
     *
     * @param ids
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:35
     **/
    @RequestMapping("/remove")
    @PreAuthorize("@am.hasPermission('role:list:remove')")
    public Result remove(@RequestBody List<String> ids) {
        sysRoleService.removeRole(ids);
        return Result.ok("删除成功");
    }
}
