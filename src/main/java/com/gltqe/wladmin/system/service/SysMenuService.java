package com.gltqe.wladmin.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gltqe.wladmin.system.entity.po.SysMenu;
import com.gltqe.wladmin.system.entity.vo.SysMenuVo;
import com.gltqe.wladmin.system.entity.dto.SysMenuDto;

import java.util.List;

/**
 * 菜单
 *
 * @author gltqe
 * @date 2022/7/3 1:59
 **/
public interface SysMenuService extends IService<SysMenu> {
    /**
     * 获取当前用户可看的所有菜单
     *
     * @return java.util.List<com.gltqe.wladmin.system.entity.po.SysMenu>
     * @author gltqe
     * @date 2022/7/3 1:59
     **/
    List<SysMenuVo> getMenuPermission();

    /**
     * 菜单查询页
     *
     * @param sysMenuDto
     * @return java.util.List<com.gltqe.wladmin.system.entity.po.SysMenu>
     * @author gltqe
     * @date 2022/7/3 2:00
     **/
    List<SysMenuVo> getMenuByUser(SysMenuDto sysMenuDto);

    /**
     * 修改状态
     *
     * @param sysMenuDto
     * @author gltqe
     * @date 2022/7/3 2:00
     **/
    void updateStatus(SysMenuDto sysMenuDto);

    /**
     * 新增
     *
     * @param sysMenuDto
     * @author gltqe
     * @date 2022/7/3 2:00
     **/
    void add(SysMenuDto sysMenuDto);

    /**
     * 删除
     *
     * @param ids
     * @author gltqe
     * @date 2022/7/3 2:00
     **/
    void remove(List<String> ids);

}
