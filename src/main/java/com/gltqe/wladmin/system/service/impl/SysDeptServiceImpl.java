package com.gltqe.wladmin.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.commons.utils.JwtUtil;
import com.gltqe.wladmin.commons.utils.TreeUtil;
import com.gltqe.wladmin.commons.exception.WlException;
import com.gltqe.wladmin.system.entity.bo.UserDetailsBo;
import com.gltqe.wladmin.system.entity.po.SysDept;
import com.gltqe.wladmin.system.entity.po.SysRole;
import com.gltqe.wladmin.system.entity.po.SysRoleDept;
import com.gltqe.wladmin.system.entity.vo.SysDeptVo;
import com.gltqe.wladmin.system.entity.dto.SysDeptDto;
import com.gltqe.wladmin.system.mapper.SysDeptMapper;
import com.gltqe.wladmin.system.mapper.SysRoleDeptMapper;
import com.gltqe.wladmin.system.service.SysDeptService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 部门
 *
 * @author gltqe
 * @date 2022/7/3 1:26
 **/
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {
    @Resource
    private SysDeptMapper sysDeptMapper;

    @Resource
    private SysRoleDeptMapper sysRoleDeptMapper;

    /**
     * 获取当前用户可以查看的所有部门
     *
     * @param sysDeptVo
     * @return java.util.List<com.gltqe.wladmin.system.entity.po.SysDept>
     * @author gltqe
     * @date 2022/7/3 1:58
     **/
    @Override
    public List<SysDeptVo> getDeptByUser(SysDeptDto sysDeptVo) {
        UserDetailsBo userDetails = JwtUtil.getUserDetails();
        List<SysRole> roleList = userDetails.getRoleList();
        Map<String, List<SysRole>> roleMap = roleList.stream().collect(Collectors.groupingBy(SysRole::getDataScope));
        boolean b = roleMap.containsKey(Constant.ALL);
        boolean admin = JwtUtil.isAdmin();
        if (!admin && !b) {
            String deptId = userDetails.getDeptId();
            Set<String> deptSet = new HashSet<>();
            deptSet.add(deptId);
            if (roleMap.containsKey(Constant.DEPT_DOWN)) {
                List<String> deptIds = sysDeptMapper.getDeptChildrenIds(deptId);
                deptSet.addAll(deptIds);
            }
            if (roleMap.containsKey(Constant.CUSTOM)) {
                List<SysRole> sysRoleList = roleMap.get(Constant.CUSTOM);
                List<String> roleIds = sysRoleList.stream().map(SysRole::getId).collect(Collectors.toList());
                LambdaQueryWrapper<SysRoleDept> rdWrapper = new LambdaQueryWrapper<>();
                rdWrapper.select(SysRoleDept::getDid).in(SysRoleDept::getRid, roleIds);
                List<Object> list = sysRoleDeptMapper.selectObjs(rdWrapper);
                for (Object o : list) {
                    deptSet.add(String.valueOf(o));
                }
            }
            sysDeptVo.setIds(deptSet.stream().toList());
        }
        List<SysDeptVo> deptList = sysDeptMapper.getDeptByUser(sysDeptVo);
        return TreeUtil.buildTree(deptList);
    }


    /**
     * 修改状态
     *
     * @param sysDeptVo
     * @author gltqe
     * @date 2022/7/3 1:26
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(SysDeptDto sysDeptVo) {
        String id = sysDeptVo.getId();
        String status = sysDeptVo.getStatus();
        if (!Constant.N.equals(status)) {
            status = Constant.Y;
        }
        LambdaUpdateWrapper<SysDept> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SysDept::getStatus, status).eq(SysDept::getId, id);
        sysDeptMapper.update(null, wrapper);
    }

    /**
     * 新增
     *
     * @param sysDeptVo
     * @author gltqe
     * @date 2022/7/3 1:26
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysDeptDto sysDeptVo) {
        SysDept sysDept = BeanUtil.copyProperties(sysDeptVo, SysDept.class);
        String parentId = sysDept.getParentId();

        // 判断不是一级部门
        if (!Constant.N.equals(parentId)) {
            SysDept dept = sysDeptMapper.selectById(parentId);
            if (dept == null) {
                throw new WlException("数据异常,未查询到上级部门,请重试或联系管理员");
            }
            if (!Constant.N.equals(dept.getStatus())) {
                throw new WlException("上级部门状态异常,禁止添加");
            }
        }

        sysDeptMapper.insert(sysDept);
    }

    /**
     * 修改
     *
     * @param sysDeptVo
     * @author gltqe
     * @date 2022/7/3 1:26
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDept(SysDeptDto sysDeptVo) {
        String parentId = sysDeptVo.getParentId();
        if (parentId.equals(sysDeptVo.getId())) {
            throw new WlException("上级部门不能选择本部门");
        }
        SysDept sysDept = BeanUtil.copyProperties(sysDeptVo, SysDept.class);
        sysDeptMapper.updateById(sysDept);
    }

    /**
     * 删除
     *
     * @param ids
     * @author gltqe
     * @date 2022/7/3 1:27
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(List<String> ids) {
        if (ids.size() == 0) {
            throw new WlException("请选择要删除的部门");
        }
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysDept::getParentId, ids);
        Long count = sysDeptMapper.selectCount(wrapper);
        if (count > 0) {
            throw new WlException("含有子部门禁止删除");
        }
        sysDeptMapper.deleteBatchIds(ids);
    }
}
