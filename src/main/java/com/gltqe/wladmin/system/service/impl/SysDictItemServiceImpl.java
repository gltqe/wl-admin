package com.gltqe.wladmin.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.commons.utils.DictUtil;
import com.gltqe.wladmin.system.entity.dto.SysDictItemDto;
import com.gltqe.wladmin.system.entity.po.SysDictItem;
import com.gltqe.wladmin.system.mapper.SysDictItemMapper;
import com.gltqe.wladmin.system.service.SysDictItemService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 字典项
 *
 * @author gltqe
 * @date 2022/7/3 1:28
 **/
@Slf4j
@Service
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements SysDictItemService {

    @Resource
    private SysDictItemMapper sysDictItemMapper;

    /**
     * 修改状态
     *
     * @param sysDictItemVo
     * @author gltqe
     * @date 2022/7/3 1:28
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(SysDictItemDto sysDictItemVo) {
        String id = sysDictItemVo.getId();
        String status = sysDictItemVo.getStatus();
        if (!Constant.N.equals(status)) {
            status = Constant.Y;
        }
        LambdaUpdateWrapper<SysDictItem> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SysDictItem::getStatus, status).eq(SysDictItem::getId, id);
        sysDictItemMapper.update(null, wrapper);
        SysDictItem sysDictItem = sysDictItemMapper.selectById(id);
        refreshDict(sysDictItem.getDictCode());

    }

    /**
     * 新增
     *
     * @param sysDictItemVo
     * @author gltqe
     * @date 2022/7/3 1:28
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDictItem(SysDictItemDto sysDictItemVo) {
        SysDictItem sysDictItem = BeanUtil.copyProperties(sysDictItemVo, SysDictItem.class);
        if (StringUtils.isBlank(sysDictItem.getStatus())){
            sysDictItem.setStatus(Constant.N);
        }
        sysDictItemMapper.insert(sysDictItem);
        if (Constant.N.equals(sysDictItem.getStatus())) {
            refreshDict(sysDictItem.getDictCode());
        }
    }


    /**
     * 修改
     *
     * @param sysDictItemVo
     * @author gltqe
     * @date 2022/7/3 1:28
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDictItem(SysDictItemDto sysDictItemVo) {
        SysDictItem sysDictItem = BeanUtil.copyProperties(sysDictItemVo, SysDictItem.class);
        String status = sysDictItem.getStatus();
        if (!Constant.N.equals(status)) {
            status = Constant.Y;
        }
        sysDictItem.setStatus(status);
        sysDictItemMapper.updateById(sysDictItem);
        SysDictItem item = sysDictItemMapper.selectById(sysDictItem.getId());
        refreshDict(item.getDictCode());
    }

    /**
     * 删除
     *
     * @param ids
     * @author gltqe
     * @date 2022/7/3 1:29
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeDictItem(List<String> ids) {
        LambdaQueryWrapper<SysDictItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(SysDictItem::getDictCode).in(SysDictItem::getId, ids).groupBy(SysDictItem::getDictCode);
        List<Object> dictCodes = sysDictItemMapper.selectObjs(wrapper);
        sysDictItemMapper.deleteBatchIds(ids);
        if (dictCodes.size() > 0) {
            for (Object code : dictCodes) {
                if (code != null) {
                    refreshDict(String.valueOf(code));
                }
            }
        }
    }

    public void refreshDict(String dictCode) {
        LambdaQueryWrapper<SysDictItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(SysDictItem::getText, SysDictItem::getValue,
                        SysDictItem::getDictCode,
                        SysDictItem::getTagType,
                        SysDictItem::getTagEffect)
                .eq(SysDictItem::getDictCode, dictCode)
                .eq(SysDictItem::getStatus, Constant.N)
                .orderByAsc(SysDictItem::getSort);
        List<SysDictItem> sysDictItems = sysDictItemMapper.selectList(wrapper);
        DictUtil.addCache(dictCode,sysDictItems);
    }

}
