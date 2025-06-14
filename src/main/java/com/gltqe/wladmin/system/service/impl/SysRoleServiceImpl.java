package com.gltqe.wladmin.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.commons.exception.WlException;
import com.gltqe.wladmin.commons.utils.JwtUtil;
import com.gltqe.wladmin.system.entity.po.SysRole;
import com.gltqe.wladmin.system.entity.po.SysRoleDept;
import com.gltqe.wladmin.system.entity.po.SysRoleMenu;
import com.gltqe.wladmin.system.entity.po.SysRoleUser;
import com.gltqe.wladmin.system.entity.vo.SysRoleVo;
import com.gltqe.wladmin.system.entity.dto.SysRoleDto;
import com.gltqe.wladmin.system.mapper.SysRoleDeptMapper;
import com.gltqe.wladmin.system.mapper.SysRoleMapper;
import com.gltqe.wladmin.system.mapper.SysRoleMenuMapper;
import com.gltqe.wladmin.system.mapper.SysRoleUserMapper;
import com.gltqe.wladmin.system.service.SysRoleDeptService;
import com.gltqe.wladmin.system.service.SysRoleMenuService;
import com.gltqe.wladmin.system.service.SysRoleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色
 *
 * @author gltqe
 * @date 2022/7/3 2:07
 **/
@Slf4j
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Resource
    private SysRoleDeptMapper sysRoleDeptMapper;
    @Resource
    private SysRoleMenuService sysRoleMenuService;
    @Resource
    private SysRoleDeptService sysRoleDeptService;

    /**
     * 分页查询
     *
     * @param sysRoleVo
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.gltqe.wladmin.system.entity.po.SysRole>
     * @author gltqe
     * @date 2022/7/3 2:06
     **/
    @Override
    public IPage<SysRoleVo> page(SysRoleDto sysRoleVo) {
        Page<SysRole> page = sysRoleVo.getPage();
        return sysRoleMapper.page(page, sysRoleVo);
    }

    /**
     * 当前用户可分配角色集合
     *
     * @return java.util.List<com.gltqe.wladmin.system.entity.po.SysRole>
     * @author gltqe
     * @date 2022/7/3 2:06
     **/
    @Override
    public List<SysRole> getRoleByUser() {
        return sysRoleMapper.getRoleByUser();
    }

    /**
     * 修改状态
     *
     * @param sysRoleDto 入参
     * @author gltqe
     * @date 2022/7/3 2:06
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(SysRoleDto sysRoleDto) {
        String id = sysRoleDto.getId();
        String status = sysRoleDto.getStatus();
        if (!Constant.N.equals(status)) {
            status = Constant.Y;
        }
        LambdaUpdateWrapper<SysRole> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SysRole::getStatus, status).eq(SysRole::getId, id);
        sysRoleMapper.update(null, wrapper);
    }

    /**
     * 新增
     *
     * @param sysRoleDto 入参
     * @author gltqe
     * @date 2022/7/3 2:07
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRole(SysRoleDto sysRoleDto) {
        checkCode(sysRoleDto);
        SysRole role = BeanUtil.copyProperties(sysRoleDto, SysRole.class);
        if (StringUtils.isBlank(role.getStatus())) {
            role.setStatus(Constant.N);
        }
        role.setCreateDept(JwtUtil.getDeptId());
        sysRoleMapper.insert(role);
        String rid = role.getId();
        List<String> menuIds = sysRoleDto.getMenuIds();
        List<String> deptIds = sysRoleDto.getDeptIds();
        List<SysRoleMenu> sysRoleMenuList = new ArrayList<>();
        for (String menuId : menuIds) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRid(rid);
            sysRoleMenu.setMid(menuId);
            sysRoleMenuList.add(sysRoleMenu);
        }
        List<SysRoleDept> sysRoleDeptList = new ArrayList<>();
        for (String deptId : deptIds) {
            SysRoleDept sysRoleDept = new SysRoleDept();
            sysRoleDept.setRid(rid);
            sysRoleDept.setDid(deptId);
            sysRoleDeptList.add(sysRoleDept);
        }
        sysRoleMenuService.saveBatch(sysRoleMenuList);
        sysRoleDeptService.saveBatch(sysRoleDeptList);

    }

    /**
     * 获取详细信息
     *
     * @param sysRoleDto 入参
     * @return com.gltqe.wladmin.system.entity.po.SysRole
     * @author gltqe
     * @date 2022/7/3 2:07
     **/
    @Override
    public SysRoleVo getOne(SysRoleDto sysRoleDto) {
        return sysRoleMapper.selectRoleMenuDept(sysRoleDto.getId());
    }

    /**
     * 修改
     *
     * @param sysRoleDto 入参
     * @author gltqe
     * @date 2022/7/3 2:07
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(SysRoleDto sysRoleDto) {
        checkCode(sysRoleDto);
        SysRole sysRole = BeanUtil.copyProperties(sysRoleDto, SysRole.class);
        sysRoleMapper.updateById(sysRole);
        String rid = sysRoleDto.getId();
        // 角色菜单
        List<String> menuIds = sysRoleDto.getMenuIds();
        if (menuIds != null) {
            //删除角色菜单关联关系
            LambdaQueryWrapper<SysRoleMenu> roleMenuWrapper = new LambdaQueryWrapper<>();
            roleMenuWrapper.eq(SysRoleMenu::getRid, rid);
            sysRoleMenuMapper.delete(roleMenuWrapper);
            // 添加角色菜单关联关系
            if (!menuIds.isEmpty()) {
                List<SysRoleMenu> roleMenuList = new ArrayList<>();
                for (String menuId : menuIds) {
                    SysRoleMenu sysRoleMenu = new SysRoleMenu();
                    sysRoleMenu.setRid(rid);
                    sysRoleMenu.setMid(menuId);
                    roleMenuList.add(sysRoleMenu);
                }
                sysRoleMenuService.saveBatch(roleMenuList);
            }
        }
        // 角色部门
        List<String> deptIds = sysRoleDto.getDeptIds();
        if (deptIds != null) {
            //删除角色部门关联关系
            LambdaQueryWrapper<SysRoleDept> roleDeptWrapper = new LambdaQueryWrapper<>();
            roleDeptWrapper.eq(SysRoleDept::getRid, rid);
            sysRoleDeptMapper.delete(roleDeptWrapper);
            if (!deptIds.isEmpty() && Constant.CUSTOM.equals(sysRoleDto.getDataScope())) {
                // 自定义数据时添加 关联部门
                List<SysRoleDept> roleDeptList = new ArrayList<>();
                for (String deptId : deptIds) {
                    SysRoleDept sysRoleDept = new SysRoleDept();
                    sysRoleDept.setDid(deptId);
                    sysRoleDept.setRid(rid);
                    roleDeptList.add(sysRoleDept);
                }
                sysRoleDeptService.saveBatch(roleDeptList);
            }
        }

    }

    /**
     * 删除
     *
     * @param ids 入参
     * @author gltqe
     * @date 2022/7/3 2:07
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRole(List<String> ids) {
        this.removeByIds(ids);
        // 删除关联关系
        LambdaQueryWrapper<SysRoleUser> ruWrapper = new LambdaQueryWrapper<>();
        ruWrapper.in(SysRoleUser::getRid, ids);
        sysRoleUserMapper.delete(ruWrapper);
        LambdaQueryWrapper<SysRoleDept> rdWrapper = new LambdaQueryWrapper<>();
        rdWrapper.in(SysRoleDept::getRid, ids);
        sysRoleDeptMapper.delete(rdWrapper);
        LambdaQueryWrapper<SysRoleMenu> rmWrapper = new LambdaQueryWrapper<>();
        rmWrapper.in(SysRoleMenu::getRid, ids);
        sysRoleMenuMapper.delete(rmWrapper);
    }

    private void checkCode(SysRoleDto sysRoleVo) {

        String code = sysRoleVo.getCode();
        if (StringUtils.isBlank(code)) {
            throw new WlException("角色编码不能为空");
        }


        String id = sysRoleVo.getId();
        if (StringUtils.isNotBlank(id)) {
            SysRole sysRole = sysRoleMapper.selectById(id);
            if (sysRole.getCode().equals(sysRoleVo.getCode())) {
                return;
            }
        }
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getCode, code);
        Long count = sysRoleMapper.selectCount(wrapper);
        if (count > 0) {
            throw new WlException("角色编码重复");
        }

    }

}
