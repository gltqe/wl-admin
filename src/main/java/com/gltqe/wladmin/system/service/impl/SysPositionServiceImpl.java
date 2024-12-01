package com.gltqe.wladmin.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.commons.exception.WlException;
import com.gltqe.wladmin.system.entity.po.SysPosition;
import com.gltqe.wladmin.system.entity.po.SysUserPosition;
import com.gltqe.wladmin.system.entity.dto.SysPositionDto;
import com.gltqe.wladmin.system.mapper.SysPositionMapper;
import com.gltqe.wladmin.system.mapper.SysUserPositionMapper;
import com.gltqe.wladmin.system.service.SysPositionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 职位
 *
 * @author gltqe
 * @date 2022/7/3 2:04
 **/
@Slf4j
@Service
public class SysPositionServiceImpl extends ServiceImpl<SysPositionMapper, SysPosition> implements SysPositionService {
    @Resource
    private SysPositionMapper sysPositionMapper;

    @Resource
    private SysUserPositionMapper sysUserPositionMapper;

    /**
     * 分页查询
     *
     * @param sysPosition
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.gltqe.wladmin.system.entity.po.SysPosition>
     * @author gltqe
     * @date 2022/7/3 2:04
     **/
    @Override
    public IPage<SysPosition> page( SysPositionDto sysPosition) {
        LambdaQueryWrapper<SysPosition> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(sysPosition.getCode()), SysPosition::getCode, sysPosition.getCode())
                .like(StringUtils.isNotBlank(sysPosition.getName()), SysPosition::getName, sysPosition.getName())
                .eq(StringUtils.isNotBlank(sysPosition.getStatus()), SysPosition::getStatus, sysPosition.getStatus())
                .orderByAsc(SysPosition::getSort);
        Page<SysPosition> positionPage = sysPositionMapper.selectPage(sysPosition.getPage(), wrapper);
        return positionPage;
    }

    /**
     * 修改状态
     *
     * @param sysPositionVo
     * @author gltqe
     * @date 2022/7/3 2:05
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(SysPositionDto sysPositionVo) {
        String id = sysPositionVo.getId();
        String status = sysPositionVo.getStatus();
        if (!Constant.N.equals(status)) {
            status = Constant.Y;
        }
        LambdaUpdateWrapper<SysPosition> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SysPosition::getStatus, status).eq(SysPosition::getId, id);
        sysPositionMapper.update(null, wrapper);
    }

    /**
     * 新增
     *
     * @param sysPositionVo
     * @author gltqe
     * @date 2022/7/3 2:05
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPosition(SysPositionDto sysPositionVo) {
        LambdaQueryWrapper<SysPosition> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysPosition::getCode, sysPositionVo.getCode());
        SysPosition sysPosition = sysPositionMapper.selectOne(wrapper);
        if (sysPosition != null) {
            throw new WlException("岗位编码重复");
        }
        SysPosition position = BeanUtil.copyProperties(sysPositionVo, SysPosition.class);
        sysPositionMapper.insert(position);
    }



    /**
     * 修改
     *
     * @param position
     * @author gltqe
     * @date 2022/7/3 2:05
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePosition(SysPositionDto position) {
        SysPosition sysPosition = BeanUtil.copyProperties(position, SysPosition.class);
        sysPositionMapper.updateById(sysPosition);
    }

    /**
     * 删除
     *
     * @param ids
     * @author gltqe
     * @date 2022/7/3 2:05
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removePosition(List<String> ids) {
        removeByIds(ids);
        // 删除关联关系
        LambdaQueryWrapper<SysUserPosition> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ids.size() > 0, SysUserPosition::getPid, ids);
        sysUserPositionMapper.delete(wrapper);
    }
}
