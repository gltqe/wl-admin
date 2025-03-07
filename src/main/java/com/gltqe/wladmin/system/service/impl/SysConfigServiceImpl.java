package com.gltqe.wladmin.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.commons.exception.WlException;
import com.gltqe.wladmin.commons.utils.ConfigUtil;
import com.gltqe.wladmin.system.entity.dto.SysConfigDto;
import com.gltqe.wladmin.system.entity.po.SysConfig;
import com.gltqe.wladmin.system.mapper.SysConfigMapper;
import com.gltqe.wladmin.system.service.SysConfigService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统配置
 *
 * @author gltqe
 * @date 2022/7/3 1:25
 **/
@Slf4j
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {
    @Resource
    private SysConfigMapper sysConfigMapper;

    /**
     * 加载核心配置到redis
     *
     * @author gltqe
     * @date 2022/7/3 1:41
     **/
    @Override
    public void loadConfig() {
        ConfigUtil.removeAllCache();
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysConfig::getStatus, Constant.N);
        List<SysConfig> sysConfigs = sysConfigMapper.selectList(wrapper);
        for (SysConfig sysConfig : sysConfigs) {
            ConfigUtil.addCache(sysConfig);
        }
    }

    /**
     * 修改状态
     *
     * @param sysConfigDto
     * @author gltqe
     * @date 2022/7/3 1:25
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(SysConfigDto sysConfigDto) {
        String id = sysConfigDto.getId();
        String status = sysConfigDto.getStatus();
        if (!Constant.N.equals(status)) {
            status = Constant.Y;
        }
        LambdaUpdateWrapper<SysConfig> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SysConfig::getStatus, status)
                .eq(SysConfig::getId, id);
        sysConfigMapper.update(null, wrapper);
        SysConfig sysConfig = sysConfigMapper.selectById(id);
        if (Constant.N.equals(status)) {
            ConfigUtil.addCache(sysConfig);
        } else {
            ConfigUtil.removeCache(sysConfig.getCode());
        }
    }

    /**
     * 新增
     *
     * @param sysConfigVo
     * @author gltqe
     * @date 2022/7/3 1:25
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addConfig(SysConfigDto sysConfigVo) {
        checkConfigKey(sysConfigVo);
        SysConfig sysConfig = BeanUtil.copyProperties(sysConfigVo, SysConfig.class);
        if (StringUtils.isBlank(sysConfig.getType())) {
            sysConfig.setType(Constant.Y);
        }
        if (StringUtils.isBlank(sysConfig.getStatus())) {
            sysConfig.setStatus(Constant.N);
        }
        sysConfigMapper.insert(sysConfig);
        if (Constant.N.equals(sysConfig.getStatus())) {
            ConfigUtil.addCache(sysConfig);
        }
    }

    /**
     * 修改
     *
     * @param sysConfigVo
     * @author gltqe
     * @date 2022/7/3 1:25
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateConfig(SysConfigDto sysConfigVo) {
        SysConfig config = sysConfigMapper.selectById(sysConfigVo.getId());
        if (!config.getCode().equals(sysConfigVo.getCode())) {
            checkConfigKey(sysConfigVo);
        }
        SysConfig sysConfig = BeanUtil.copyProperties(sysConfigVo, SysConfig.class);
        String status = sysConfig.getStatus();
        if (!Constant.N.equals(status)) {
            status = Constant.Y;
        }
        sysConfigMapper.updateById(sysConfig);
        if (Constant.N.equals(status)) {
            ConfigUtil.addCache(sysConfig);
        } else {
            ConfigUtil.removeCache(sysConfig.getCode());
        }
    }

    /**
     * 删除
     *
     * @param keys
     * @author gltqe
     * @date 2022/7/3 1:25
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeConfig(List<String> keys) {
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysConfig::getCode, keys);
        sysConfigMapper.delete(wrapper);
        for (String key : keys) {
           ConfigUtil.removeCache(key);
        }
    }

    /**
     * 刷新redis
     *
     * @author gltqe
     * @date 2022/7/3 1:58
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refreshConfig() {
        loadConfig();
    }

    public void checkConfigKey(SysConfigDto sysConfigVo) {
        if (StringUtils.isBlank(sysConfigVo.getCode())) {
            throw new WlException("参数键不能为空");
        }
        String id = sysConfigVo.getId();
        if (StringUtils.isNotBlank(id)) {
            SysConfig sysConfig = sysConfigMapper.selectById(id);
            if (sysConfig.getCode().equals(sysConfigVo.getCode())) {
                return;
            }
        }
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysConfig::getCode, sysConfigVo.getCode());
        Long count = sysConfigMapper.selectCount(wrapper);
        if (count > 0) {
            throw new WlException("参数编码已存在,禁止重复添加");
        }


    }
}
