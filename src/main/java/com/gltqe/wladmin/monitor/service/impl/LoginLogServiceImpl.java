package com.gltqe.wladmin.monitor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.monitor.entity.po.LogLogin;
import com.gltqe.wladmin.monitor.entity.vo.LoginLogVo;
import com.gltqe.wladmin.monitor.entity.dto.LoginLogDto;
import com.gltqe.wladmin.monitor.mapper.LoginLogMapper;
import com.gltqe.wladmin.monitor.service.LoginLogService;
import com.gltqe.wladmin.system.entity.po.SysDept;
import com.gltqe.wladmin.system.entity.po.SysUser;
import com.gltqe.wladmin.system.mapper.SysDeptMapper;
import com.gltqe.wladmin.system.mapper.SysUserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LogLogin> implements LoginLogService {

    @Resource
    private LoginLogMapper loginLogMapper;
    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysUserMapper sysUserMapper;

    /**
     * 分页查询
     *
     * @param loginLogDto
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.gltqe.wladmin.monitor.entity.LogLogin>
     * @author gltqe
     * @date 2022/7/3 0:51
     **/
    @Override
    public IPage<LoginLogVo> page(LoginLogDto loginLogDto) {
        Page<LogLogin> page = loginLogDto.getPage();
        Page<LoginLogVo> loginLogPage = loginLogMapper.page(page, loginLogDto);
        return loginLogPage;
    }

    /**
     * 获取登录日志详细信息
     *
     * @param id
     * @return com.gltqe.wladmin.monitor.entity.LogLogin
     * @author gltqe
     * @date 2022/7/3 0:51
     **/
    @Override
    public LoginLogVo getLogLogin(String id) {
        LogLogin loginLog = loginLogMapper.selectById(id);
        LoginLogVo loginDto = BeanUtil.copyProperties(loginLog, LoginLogVo.class);
        String deptId = loginLog.getDeptId();
        if (!Constant.N.equals(deptId)) {
            SysDept sysDept = sysDeptMapper.selectById(deptId);
            if (sysDept != null) {
                String name = sysDept.getName();
                loginDto.setDeptName(name);
            }
        }
        String uid = loginLog.getUid();
        SysUser sysUser = sysUserMapper.selectById(uid);
        loginDto.setCnName(sysUser.getCnName());
        return loginDto;
    }

    /**
     * 删除日志
     *
     * @param ids
     * @author gltqe
     * @date 2022/7/3 0:51
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeLog(List<String> ids) {
        loginLogMapper.deleteBatchIds(ids);
    }

}
