package com.gltqe.wladmin.framework.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gltqe.wladmin.commons.exception.LoginException;
import com.gltqe.wladmin.system.entity.po.SysUser;
import com.gltqe.wladmin.system.entity.bo.UserDetailsBo;
import com.gltqe.wladmin.system.mapper.SysUserMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


/**
 * 认证的用户信息来源
 *
 * @author gltqe
 * @date 2022/7/3 1:07
 **/
@Slf4j
@Service
public class UserDetailServiceImpl implements UserDetailsService {


    @Resource
    private SysUserMapper sysUserMapper;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("查询用户名:" + username);
        // 根据用户名去数据库查询
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(SysUser::getId,
                        SysUser::getUsername,
                        SysUser::getPassword,
                        SysUser::getSalt,
                        SysUser::getStatus,
                        SysUser::getDeptId)
                .eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);

        if (Objects.isNull(sysUser)) {
            throw new LoginException("用户名或密码错误!");
        }

        UserDetailsBo user = new UserDetailsBo(sysUser.getId(),
                sysUser.getUsername(),
                sysUser.getPassword(),
                sysUser.getSalt(),
                sysUser.getStatus(),
                sysUser.getDeptId()
        );
        return user;
    }
}
