package com.gltqe.wladmin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gltqe.wladmin.system.entity.po.SysRole;
import com.gltqe.wladmin.system.entity.vo.SysRoleVo;
import com.gltqe.wladmin.system.entity.dto.SysRoleDto;

import java.util.List;

/**
 * 角色
 *
 * @author gltqe
 * @date 2022/7/3 2:05
 **/
public interface SysRoleService extends IService<SysRole> {
    /**
     * 分页查询 只能查看当前用户拥有的角色
     *
     * @param sysRoleDto
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.gltqe.wladmin.system.entity.po.SysRole>
     * @author gltqe
     * @date 2022/7/3 2:06
     **/
    IPage<SysRoleVo> page(SysRoleDto sysRoleDto);

    /**
     * 获取当前用户可看到的角色
     *
     * @return java.util.List<com.gltqe.wladmin.system.entity.po.SysRole>
     * @author gltqe
     * @date 2022/7/3 2:06
     **/
    List<SysRole> getRoleByUser();

    /**
     * 修改状态
     *
     * @param sysRoleDto
     * @author gltqe
     * @date 2022/7/3 2:06
     **/
    void updateStatus(SysRoleDto sysRoleDto);

    /**
     * 新增
     *
     * @param sysRoleDto
     * @author gltqe
     * @date 2022/7/3 2:07
     **/
    void addRole(SysRoleDto sysRoleDto);

    /**
     * 获取详细信息
     *
     * @param sysRoleDto
     * @return com.gltqe.wladmin.system.entity.po.SysRole
     * @author gltqe
     * @date 2022/7/3 2:07
     **/
    SysRoleVo getOne(SysRoleDto sysRoleDto);

    /**
     * 修改
     *
     * @param sysRoleDto
     * @author gltqe
     * @date 2022/7/3 2:07
     **/
    void updateRole(SysRoleDto sysRoleDto);

    /**
     * 删除
     *
     * @param ids
     * @author gltqe
     * @date 2022/7/3 2:07
     **/
    void removeRole(List<String> ids);
}
