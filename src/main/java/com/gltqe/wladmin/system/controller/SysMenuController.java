package com.gltqe.wladmin.system.controller;

import cn.hutool.core.bean.BeanUtil;
import com.gltqe.wladmin.commons.common.Result;

import com.gltqe.wladmin.system.entity.po.SysMenu;
import com.gltqe.wladmin.system.entity.vo.SysMenuVo;
import com.gltqe.wladmin.system.entity.dto.SysMenuDto;
import com.gltqe.wladmin.system.service.SysMenuService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜单
 *
 * @author gltqe
 * @date 2022/7/3 1:29
 **/
@RestController
@RequestMapping("/sysMenu")
public class SysMenuController {
    @Resource
    private SysMenuService sysMenuService;

    /**
     * 获取当前用户可看的所有菜单
     *
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:29
     **/
    @RequestMapping("/getMenuPermission")
    public Result getMenuPermission() {
        List<SysMenuVo> sysMenuList = sysMenuService.getMenuPermission();
        return Result.ok(sysMenuList);
    }

    /**
     * 菜单查询页
     *
     * @param sysMenuDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:29
     **/
    @RequestMapping("/getMenuByUser")
    @PreAuthorize("@am.hasPermission('menu:list:query')")
    public Result getMenuByUser(@RequestBody SysMenuDto sysMenuDto) {
        List<SysMenuVo> sysMenuList = sysMenuService.getMenuByUser(sysMenuDto);
        return Result.ok(sysMenuList);
    }

    /**
     * 修改状态
     *
     * @param sysMenuDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:29
     **/
    @RequestMapping("/updateStatus")
    @PreAuthorize("@am.hasPermission('menu:list:update')")
    public Result updateStatus(@RequestBody SysMenuDto sysMenuDto) {
        sysMenuService.updateStatus(sysMenuDto);
        return Result.ok();
    }

    /**
     * 新增
     *
     * @param sysMenuDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:30
     **/
    @RequestMapping("/add")
    @PreAuthorize("@am.hasPermission('menu:list:add')")
    public Result add(@RequestBody SysMenuDto sysMenuDto) {
        sysMenuService.add(sysMenuDto);
        return Result.ok("新增成功");
    }

    /**
     * 获取详细信息
     *
     * @param sysMenuDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:30
     **/
    @RequestMapping("/getOne")
    @PreAuthorize("@am.hasPermission('menu:list:query')")
    public Result getOne(@RequestBody SysMenuDto sysMenuDto) {
        SysMenu menu = sysMenuService.getById(sysMenuDto.getId());
        SysMenuVo sysMenuVo = BeanUtil.copyProperties(menu, SysMenuVo.class);
        return Result.ok(sysMenuVo);
    }

    /**
     * 修改
     *
     * @param sysMenuDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:30
     **/
    @RequestMapping("/update")
    @PreAuthorize("@am.hasPermission('menu:list:update')")
    public Result update(@RequestBody SysMenuDto sysMenuDto) {
        SysMenu sysMenu = BeanUtil.copyProperties(sysMenuDto, SysMenu.class);
        sysMenuService.updateById(sysMenu);
        return Result.ok();
    }

    /**
     * 删除
     *
     * @param ids
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:30
     **/
    @RequestMapping("/remove")
    @PreAuthorize("@am.hasPermission('menu:list:remove')")
    public Result remove(@RequestBody List<String> ids) {
        sysMenuService.remove(ids);
        return Result.ok("删除成功");
    }
}
