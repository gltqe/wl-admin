package com.gltqe.wladmin.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.commons.exception.WlException;
import com.gltqe.wladmin.commons.utils.DictUtil;
import com.gltqe.wladmin.system.entity.dto.SysDictDto;
import com.gltqe.wladmin.system.entity.po.SysDict;
import com.gltqe.wladmin.system.entity.po.SysDictItem;
import com.gltqe.wladmin.system.mapper.SysDictItemMapper;
import com.gltqe.wladmin.system.mapper.SysDictMapper;
import com.gltqe.wladmin.system.service.SysDictService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {
    @Resource
    private SysDictMapper sysDictMapper;
    @Resource
    private SysDictItemMapper sysDictItemMapper;

    /**
     * 加载字典到redis
     *
     * @author gltqe
     * @date 2022/7/3 1:54
     **/
    @Override
    public void loadDict() {
        DictUtil.removeAllCache();
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(SysDict::getCode).eq(SysDict::getStatus, Constant.N);
        List<SysDict> sysDictList = sysDictMapper.selectList(wrapper);
        for (SysDict sysDict : sysDictList) {
            initDict(sysDict);
        }
    }

    /**
     * 修改状态
     *
     * @param sysDictVo
     * @author gltqe
     * @date 2022/7/3 1:55
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(SysDictDto sysDictVo) {
        String id = sysDictVo.getId();
        String status = sysDictVo.getStatus();
        if (!Constant.N.equals(status)) {
            status = Constant.Y;
        }
        LambdaUpdateWrapper<SysDict> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SysDict::getStatus, status).eq(SysDict::getId, id);
        sysDictMapper.update(null, wrapper);
        SysDict sysDict = sysDictMapper.selectById(id);
        if (Constant.N.equals(status)) {
            initDict(sysDict);
        } else {
            DictUtil.removeCache(sysDict.getCode());
        }
    }

    /**
     * 新增
     *
     * @param sysDictVo
     * @author gltqe
     * @date 2022/7/3 1:55
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDict(SysDictDto sysDictVo) {
        checkDictKey(sysDictVo);
        SysDict sysDict = BeanUtil.copyProperties(sysDictVo, SysDict.class);
        if (StringUtils.isBlank(sysDict.getStatus())){
            sysDict.setStatus(Constant.N);
        }
        sysDictMapper.insert(sysDict);
    }

    /**
     * 修改
     *
     * @param sysDictVo
     * @author gltqe
     * @date 2022/7/3 1:55
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDict(SysDictDto sysDictVo) {
        SysDict dict = sysDictMapper.selectById(sysDictVo.getId());
        if (StringUtils.isNotBlank(sysDictVo.getCode()) && !dict.getCode().equals(sysDictVo.getCode())) {
            checkDictKey(sysDictVo);
            //修改code,同步sys_dict_item
            LambdaUpdateWrapper<SysDictItem> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(SysDictItem::getDictCode, sysDictVo.getCode()).eq(SysDictItem::getDictCode, dict.getCode());
            sysDictItemMapper.update(null, wrapper);
        }
        SysDict sysDict = BeanUtil.copyProperties(sysDictVo, SysDict.class);
        sysDictMapper.updateById(sysDict);
        String status = sysDict.getStatus();
        if (Constant.N.equals(status)) {
            initDict(sysDict);
        } else {
            // 清理之前的code
            DictUtil.removeCache(sysDict.getCode());
        }
    }

    /**
     * 删除
     *
     * @param keys
     * @author gltqe
     * @date 2022/7/3 1:55
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeDict(List<String> keys) {
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysDict::getCode, keys);
        sysDictMapper.delete(wrapper);
        LambdaQueryWrapper<SysDictItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysDictItem::getDictCode, keys);
        sysDictItemMapper.delete(queryWrapper);
        for (String key : keys) {
            DictUtil.removeCache(key);
        }
    }

    /**
     * 刷新redis
     *
     * @author gltqe
     * @date 2022/7/3 1:55
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refreshDict() {
        loadDict();
    }

    public void initDict(SysDict sysDict) {
        LambdaQueryWrapper<SysDictItem> wrapper = new LambdaQueryWrapper();
        wrapper.select(SysDictItem::getText, SysDictItem::getValue, SysDictItem::getDictCode,SysDictItem::getTagType,SysDictItem::getTagEffect)
                .eq(SysDictItem::getDictCode, sysDict.getCode())
                .eq(SysDictItem::getStatus, Constant.N)
                .orderByAsc(SysDictItem::getSort);
        List<SysDictItem> sysDictItems = sysDictItemMapper.selectList(wrapper);
        DictUtil.addCache(sysDict.getCode(),sysDictItems);
    }

    public void checkDictKey(SysDictDto sysDict) {
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDict::getCode, sysDict.getCode());
        Long count = sysDictMapper.selectCount(wrapper);
        if (count > 0) {
            throw new WlException("字典编码已存在,禁止重复添加");
        }
    }
}
