package com.gltqe.wladmin.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.commons.enums.ExportTypeEnum;
import com.gltqe.wladmin.commons.exception.WlException;
import com.gltqe.wladmin.commons.utils.ExcelUtil;
import com.gltqe.wladmin.system.entity.dto.SysPositionDto;
import com.gltqe.wladmin.system.entity.po.SysPosition;
import com.gltqe.wladmin.system.entity.po.SysUserPosition;
import com.gltqe.wladmin.system.entity.vo.SysPositionVo;
import com.gltqe.wladmin.system.mapper.SysPositionMapper;
import com.gltqe.wladmin.system.mapper.SysUserPositionMapper;
import com.gltqe.wladmin.system.service.SysPositionService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
     * @param sysPositionDto
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.gltqe.wladmin.system.entity.po.SysPositionVo>
     * @author gltqe
     * @date 2022/7/3 2:04
     **/
    @Override
    public IPage<SysPosition> page(SysPositionDto sysPositionDto) {
        LambdaQueryWrapper<SysPosition> wrapper =getPageQuery(sysPositionDto);
        Page<SysPosition> positionPage = sysPositionMapper.selectPage(sysPositionDto.getPage(), wrapper);
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
        wrapper.in(!ids.isEmpty(), SysUserPosition::getPid, ids);
        sysUserPositionMapper.delete(wrapper);
    }

    /**
     * 导出
     *
     * @param sysPositionDto
     * @author gltqe
     * @date 2025/2/23 18:10
     */
    @Override
    public void exportPosition(SysPositionDto sysPositionDto, HttpServletResponse response) {
        List<SysPosition> list = new ArrayList<>();
        Integer exportType = sysPositionDto.getExportType();
        if (ExportTypeEnum.CURRENT_PAGE.getCode().equals(exportType)) {
            IPage<SysPosition> positionIPage = page(sysPositionDto);
            list= positionIPage.getRecords();
        } else if (ExportTypeEnum.QUERY_ALL.getCode().equals(exportType)) {
            list = list(getPageQuery(sysPositionDto));
        } else if (ExportTypeEnum.ALL.getCode().equals(exportType)) {
            list = list();
        } else if (ExportTypeEnum.SELECT_DATA.getCode().equals(exportType)) {
            List<String> ids = sysPositionDto.getIds();
            if (ids != null && !ids.isEmpty()) {
                list = baseMapper.selectBatchIds(ids);
            }
        } else {
            throw new WlException("错误的导出类型");
        }
        List<SysPositionVo> voList = new ArrayList<>();
        for (SysPosition sysPosition : list) {
            SysPositionVo sysPositionVo = new SysPositionVo();
            BeanUtils.copyProperties(sysPosition, sysPositionVo);
            voList.add(sysPositionVo);
        }
        ExcelUtil.writeExcel(voList, "用户信息", "用户信息", response, SysPositionVo.class);
    }

    private LambdaQueryWrapper<SysPosition> getPageQuery(SysPositionDto sysPositionDto) {
        LambdaQueryWrapper<SysPosition> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(sysPositionDto.getCode()), SysPosition::getCode, sysPositionDto.getCode())
                .like(StringUtils.isNotBlank(sysPositionDto.getName()), SysPosition::getName, sysPositionDto.getName())
                .eq(StringUtils.isNotBlank(sysPositionDto.getStatus()), SysPosition::getStatus, sysPositionDto.getStatus())
                .orderByAsc(SysPosition::getSort);
        return wrapper;
    }
}
