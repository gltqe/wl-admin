package com.gltqe.wladmin.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.commons.utils.JwtUtil;
import com.gltqe.wladmin.commons.utils.TreeUtil;
import com.gltqe.wladmin.commons.exception.WlException;
import com.gltqe.wladmin.system.entity.po.SysMenu;
import com.gltqe.wladmin.system.entity.vo.SysMenuVo;
import com.gltqe.wladmin.system.entity.dto.SysMenuDto;
import com.gltqe.wladmin.system.mapper.SysMenuMapper;
import com.gltqe.wladmin.system.mapper.SysRoleMapper;
import com.gltqe.wladmin.system.mapper.SysRoleMenuMapper;
import com.gltqe.wladmin.system.service.SysMenuService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单
 *
 * @author gltqe
 * @date 2022/7/3 2:00
 **/
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    /**
     * 获取当前用户可看的所有菜单
     *
     * @return java.util.List<com.gltqe.wladmin.system.entity.po.SysMenu>
     * @author gltqe
     * @date 2022/7/3 2:00
     **/
    @Override
    public List<SysMenuVo> getMenuPermission() {
        String userId = JwtUtil.getUserId();
        List<SysMenuVo> menus = new ArrayList<>();
        if (JwtUtil.isAdmin()) {
            SysMenu sysMenu = new SysMenu();
            sysMenu.setStatus(Constant.N);
            menus = sysMenuMapper.getAllMenus(sysMenu);
        } else {
            menus = sysMenuMapper.getMenuPermission(userId);
        }
        List<SysMenuVo> list = TreeUtil.buildTree(menus, "0");
        return list;
    }

    /**
     * 菜单查询页
     *
     * @param sysMenuVo
     * @return java.util.List<com.gltqe.wladmin.system.entity.po.SysMenu>
     * @author gltqe
     * @date 2022/7/3 2:01
     **/
    @Override
    public List<SysMenuVo> getMenuByUser(SysMenuDto sysMenuVo) {
        String userId = JwtUtil.getUserId();
        List<SysMenuVo> menus = new ArrayList<>();
        if (JwtUtil.isAdmin()) {
            SysMenu sysMenu = BeanUtil.copyProperties(sysMenuVo, SysMenu.class);
            menus = sysMenuMapper.getAllMenus(sysMenu);
        } else {
            menus = sysMenuMapper.getMenuByUser(userId, sysMenuVo);
        }
        List<SysMenuVo> list = TreeUtil.buildTree(menus);
        return list;
    }

    /**
     * 修改状态
     *
     * @param sysMenuVo
     * @author gltqe
     * @date 2022/7/3 2:01
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(SysMenuDto sysMenuVo) {
        String id = sysMenuVo.getId();
        String status = sysMenuVo.getStatus();
        if (!Constant.N.equals(status)) {
            status = Constant.Y;
        }
        LambdaUpdateWrapper<SysMenu> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SysMenu::getStatus, status).eq(SysMenu::getId, id);
        sysMenuMapper.update(null, wrapper);
    }

    /**
     * 新增
     *
     * @param sysMenuVo
     * @author gltqe
     * @date 2022/7/3 2:01
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysMenuDto sysMenuVo) {
        SysMenu sysMenu = BeanUtil.copyProperties(sysMenuVo, SysMenu.class);
        String status = sysMenu.getStatus();
        if (StringUtils.isBlank(status)) {
            sysMenu.setStatus(Constant.N);
        }
        sysMenuMapper.insert(sysMenu);
    }

    /**
     * 删除
     *
     * @param ids
     * @author gltqe
     * @date 2022/7/3 2:01
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(List<String> ids) {
        if (ids.size() == 0) {
            throw new WlException("请选择要删除的菜单");
        }
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysMenu::getParentId, ids);
        Long count = sysMenuMapper.selectCount(wrapper);
        if (count > 0) {
            throw new WlException("含有子菜单禁止删除");
        }
        sysMenuMapper.deleteBatchIds(ids);
    }

}
