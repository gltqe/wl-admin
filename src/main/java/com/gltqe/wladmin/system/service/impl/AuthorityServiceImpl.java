package com.gltqe.wladmin.system.service.impl;

import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.commons.exception.LoginException;
import com.gltqe.wladmin.commons.utils.JwtUtil;
import com.gltqe.wladmin.system.entity.bo.UserDetailsBo;
import com.gltqe.wladmin.system.entity.po.SysRole;
import com.gltqe.wladmin.system.mapper.SysRoleMapper;
import com.gltqe.wladmin.system.mapper.SysUserMapper;
import com.gltqe.wladmin.system.service.AuthorityService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public void setRolePermission(UserDetailsBo userDetailsBo) {
        // 查询角色
        String uid = userDetailsBo.getUserId();
        List<SysRole> roleList = sysRoleMapper.getUserRoleByUserId(uid);
        userDetailsBo.setRoleList(roleList);
        // 查询权限
        List<String> permissionList = new ArrayList<>();
        String username = userDetailsBo.getUsername();
        if (JwtUtil.isAdmin(username)) {
            permissionList.add(Constant.ADMIN_PERMISSION);
        } else {
            if (roleList.isEmpty()) {
                throw new LoginException("用户状态异常,未分配任何角色,请联系管理员");
            }
            permissionList = sysUserMapper.getPermissionByUser(uid);
        }
        userDetailsBo.setPermissionList(permissionList);
    }
}
