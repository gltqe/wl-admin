package com.gltqe.wladmin.monitor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.monitor.entity.po.LogOperation;
import com.gltqe.wladmin.monitor.entity.vo.OperationLogVo;
import com.gltqe.wladmin.monitor.entity.dto.OperationLogDto;
import com.gltqe.wladmin.monitor.mapper.OperationLogMapper;
import com.gltqe.wladmin.monitor.service.OperationLogService;
import com.gltqe.wladmin.system.entity.po.SysDept;
import com.gltqe.wladmin.system.entity.po.SysUser;
import com.gltqe.wladmin.system.mapper.SysDeptMapper;
import com.gltqe.wladmin.system.mapper.SysUserMapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, LogOperation> implements OperationLogService {

    @Resource
    private OperationLogMapper operationLogMapper;

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    /**
     * 分页查询
     *
     * @param operationLogVo
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.gltqe.wladmin.monitor.entity.po.OperationLog>
     * @author gltqe
     * @date 2022/7/3 0:52
     **/
    @Override
    public IPage<OperationLogVo> page(OperationLogDto operationLogDto) {
//        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
//        wrapper.like(StringUtils.isNotBlank(operationLogVo.getName()), OperationLog::getName, operationLogVo.getName())
//                .like(StringUtils.isNotBlank(operationLogVo.getOperator()), OperationLog::getOperator, operationLogVo.getOperator())
//                .eq(StringUtils.isNotBlank(operationLogVo.getType()), OperationLog::getType, operationLogVo.getType())
//                .eq(StringUtils.isNotBlank(operationLogVo.getStatus()), OperationLog::getStatus, operationLogVo.getStatus())
//                .between(StringUtils.isNotBlank(operationLogVo.getStartDateTime()) && StringUtils.isNotBlank(operationLogVo.getEndDateTime()),
//                        OperationLog::getRequestTime, operationLogVo.getStartDateTime(), operationLogVo.getEndDateTime())
//                .orderByDesc(OperationLog::getRequestTime);
        Page<LogOperation> page = operationLogDto.getPage();
        return operationLogMapper.page(page, operationLogDto);
    }

    /**
     * 获取操作日志详细信息
     *
     * @param id
     * @return com.gltqe.wladmin.monitor.entity.po.OperationLog
     * @author gltqe
     * @date 2022/7/3 0:52
     **/
    @Override
    public OperationLogVo getOperationLog(String id) {
        LogOperation operationLog = operationLogMapper.selectById(id);
        OperationLogVo operationLogVo = BeanUtil.copyProperties(operationLog, OperationLogVo.class);
        String deptId = operationLog.getDeptId();
        if (!Constant.N.equals(deptId)) {
            SysDept sysDept = sysDeptMapper.selectById(deptId);
            if (sysDept != null) {
                String name = sysDept.getName();
                operationLogVo.setDeptName(name);
            }
        }
        String username = operationLog.getOperator();
        if (StringUtils.isNotBlank(username)) {
            LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysUser::getUsername, username);
            SysUser sysUser = sysUserMapper.selectOne(wrapper);
            operationLogVo.setCnName(sysUser.getCnName());
        }
        return operationLogVo;
    }

    /**
     * 删除日志
     *
     * @param ids
     * @author gltqe
     * @date 2022/7/3 0:52
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeLog(List<String> ids) {
        operationLogMapper.deleteBatchIds(ids);
    }
}
