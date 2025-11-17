package com.gltqe.wladmin.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.commons.enums.TimeUnitEnum;
import com.gltqe.wladmin.commons.exception.WlException;
import com.gltqe.wladmin.commons.utils.TimeUnitUtil;
import com.gltqe.wladmin.system.entity.po.SysApiLimit;
import com.gltqe.wladmin.system.entity.dto.SysApiLimitDto;
import com.gltqe.wladmin.system.mapper.SysApiLimitMapper;
import com.gltqe.wladmin.system.service.SysApiLimitService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 系统配置
 *
 * @author gltqe
 * @date 2022/7/3 1:25
 **/
@Slf4j
@Service
public class SysApiLimitServiceImpl extends ServiceImpl<SysApiLimitMapper, SysApiLimit> implements SysApiLimitService {

    @Resource
    private SysApiLimitMapper sysApiLimitMapper;
    @Resource
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 加载核心配置到内存
     *
     * @author gltqe
     * @date 2022/7/3 1:41
     **/
    @Override
    public void loadApiLimit() {
        redisTemplate.delete(Constant.LIMIT_URL_KEY);
        Set<String> wholeKeys = redisTemplate.keys(Constant.LIMIT_WHOLE_KEY + "*");
        Set<String> singleKeys = redisTemplate.keys(Constant.LIMIT_SINGLE_KEY + "*");
        Collection<String> union = CollUtil.union(wholeKeys, singleKeys);
        redisTemplate.delete(union);
        LambdaQueryWrapper<SysApiLimit> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(SysApiLimit::getName,
                SysApiLimit::getUri,
                SysApiLimit::getSingleFrequency,
                SysApiLimit::getSingleTimeSecond,
                SysApiLimit::getSingleLimiterRate,
                SysApiLimit::getWholeFrequency,
                SysApiLimit::getWholeTimeSecond,
                SysApiLimit::getWholeLimiterRate).eq(SysApiLimit::getStatus, Constant.N);
        List<SysApiLimit> sysApiLimits = sysApiLimitMapper.selectList(wrapper);
        for (SysApiLimit sysApiLimit : sysApiLimits) {
            initApiLimit(sysApiLimit);
        }
    }


    /**
     * 修改状态
     *
     * @param sysApiLimitVo
     * @author gltqe
     * @date 2022/7/3 1:25
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(SysApiLimitDto sysApiLimitVo) {
        String id = sysApiLimitVo.getId();
        String status = sysApiLimitVo.getStatus();
        LambdaUpdateWrapper<SysApiLimit> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SysApiLimit::getStatus, status).eq(SysApiLimit::getId, id);
        sysApiLimitMapper.update(null, wrapper);
        SysApiLimit sysApiLimit = sysApiLimitMapper.selectById(id);
        if (Constant.N.equals(status)) {
            initApiLimit(sysApiLimit);
        } else {
            String uri = sysApiLimit.getUri();
            cleanApiLimit(uri);
        }
    }

    /**
     * 新增
     *
     * @param sysApiLimitVo
     * @author gltqe
     * @date 2022/7/3 1:25
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addApiLimit(SysApiLimitDto sysApiLimitVo) {
        checkApiLimitKey(sysApiLimitVo);
        SysApiLimit sysApiLimit = BeanUtil.copyProperties(sysApiLimitVo, SysApiLimit.class);
        convertLimiter(sysApiLimit);
        sysApiLimitMapper.insert(sysApiLimit);
        if (Constant.N.equals(sysApiLimit.getStatus())) {
            initApiLimit(sysApiLimit);
        }
    }

    /**
     * 修改
     *
     * @param sysApiLimitVo
     * @author gltqe
     * @date 2022/7/3 1:25
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateApiLimit(SysApiLimitDto sysApiLimitVo) {
        checkApiLimitKey(sysApiLimitVo);
        SysApiLimit sysApiLimit = BeanUtil.copyProperties(sysApiLimitVo, SysApiLimit.class);
        convertLimiter(sysApiLimit);
        sysApiLimitMapper.updateById(sysApiLimit);
        String status = sysApiLimit.getStatus();
        if (Constant.N.equals(status)) {
            initApiLimit(sysApiLimit);
        } else {
            String uri = sysApiLimit.getUri();
            cleanApiLimit(uri);
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
    public void removeApiLimitByUri(List<String> keys) {
        LambdaQueryWrapper<SysApiLimit> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysApiLimit::getUri, keys);
        sysApiLimitMapper.delete(wrapper);
        for (String key : keys) {
            cleanApiLimit(key);
        }
    }

    /**
     * 转换次数和时间
     *
     * @author gltqe
     * @date 2022/7/3 1:58
     **/
    public void convertLimiter(SysApiLimit sysApiLimit) {
        Long singleFrequency = sysApiLimit.getSingleFrequency();
        String singleTime = sysApiLimit.getSingleTime();
        if (singleFrequency != null && singleFrequency > 0 && StringUtils.isNotBlank(singleTime)) {
            Long timeUnit = TimeUnitEnum.getValue(sysApiLimit.getSingleTimeUnit());
            // 时间转换为秒
            BigDecimal second = TimeUnitUtil.convert(new BigDecimal(singleTime), timeUnit, TimeUnitUtil.SECOND, 0);
            BigDecimal limiter = new BigDecimal(String.valueOf(singleFrequency)).divide(second, Constant.LIMITER_SCALE, BigDecimal.ROUND_HALF_UP);
            if (limiter.compareTo(new BigDecimal(Constant.LIMIT_LOWEST_RATE)) <= 0) {
                throw new WlException("接口限制需要大于" + Constant.LIMIT_LOWEST_RATE + "次/秒");
            }
            sysApiLimit.setSingleTimeSecond(second.toBigInteger().longValue());
            sysApiLimit.setSingleLimiterRate(limiter.toPlainString());
        }
        Long wholeFrequency = sysApiLimit.getWholeFrequency();
        String wholeTime = sysApiLimit.getWholeTime();
        if (wholeFrequency != null && wholeFrequency > 0 && StringUtils.isNotBlank(wholeTime)) {
            Long timeUnit = TimeUnitEnum.getValue(sysApiLimit.getWholeTimeUnit());
            // 时间转换为秒
            BigDecimal second = TimeUnitUtil.convert(new BigDecimal(wholeTime), timeUnit, TimeUnitUtil.SECOND, 0);
            BigDecimal limiter = new BigDecimal(String.valueOf(wholeFrequency)).divide(second, Constant.LIMITER_SCALE, BigDecimal.ROUND_HALF_UP);
            if (limiter.compareTo(new BigDecimal(Constant.LIMIT_LOWEST_RATE)) <= 0) {
                throw new WlException("接口限制需要大于" + Constant.LIMIT_LOWEST_RATE + "次/秒");
            }
            sysApiLimit.setWholeTimeSecond(second.toBigInteger().longValue());
            sysApiLimit.setWholeLimiterRate(limiter.toPlainString());
        }

        String singleLimiter = sysApiLimit.getSingleLimiterRate();
        String wholeLimiter = sysApiLimit.getWholeLimiterRate();
        if (StringUtils.isBlank(singleLimiter) && StringUtils.isBlank(wholeLimiter)) {
            // 都为空则设置默认
            sysApiLimit.setSingleLimiterRate(Constant.LIMIT_DEFAULT_SINGLE);
            sysApiLimit.setWholeLimiterRate(Constant.LIMIT_DEFAULT_WHOLE);
        }
    }

    /**
     * 刷新
     *
     * @author gltqe
     * @date 2022/7/3 1:58
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refreshApiLimit() {
        loadApiLimit();
    }

    public void initApiLimit(SysApiLimit sysApiLimit) {
        String uri = sysApiLimit.getUri();
        cleanApiLimit(uri);
        redisTemplate.opsForSet().add(Constant.LIMIT_URL_KEY, uri);
        String singleLimiterRate = sysApiLimit.getSingleLimiterRate();
        Long singleTimeSecond = sysApiLimit.getSingleTimeSecond();
        String wholeLimiterRate = sysApiLimit.getWholeLimiterRate();
        boolean single = StringUtils.isNotBlank(singleLimiterRate) && singleTimeSecond != null && singleTimeSecond > 0;
        if (single) {
            String singleKey = Constant.LIMIT_SINGLE_KEY + uri;
            BoundHashOperations<String, String, Object> hashOps = redisTemplate.boundHashOps(singleKey);
            hashOps.put("size", sysApiLimit.getSingleFrequency());
//            hashOps.put("rate", singleLimiterRate);
            hashOps.put("time", singleTimeSecond);
            hashOps.put("timeMicro", TimeUnitUtil.convert(new BigDecimal(singleTimeSecond), TimeUnitUtil.SECOND, TimeUnitUtil.MICROSECOND, 0));
        }
        boolean whole = StringUtils.isNotBlank(wholeLimiterRate);
        if (whole) {
            String wholeKey = Constant.LIMIT_WHOLE_KEY + uri;
            BoundHashOperations<String, String, Object> hashOps = redisTemplate.boundHashOps(wholeKey);
            hashOps.put("size", sysApiLimit.getWholeFrequency());
//            hashOps.put("rate", wholeLimiterRate);
            hashOps.put("time", sysApiLimit.getWholeTimeSecond());
            hashOps.put("timeMicro", TimeUnitUtil.convert(new BigDecimal(sysApiLimit.getWholeTimeSecond()), TimeUnitUtil.SECOND, TimeUnitUtil.MICROSECOND, 0));
        }
    }

    public void cleanApiLimit(String uri) {
        SetOperations<String,String> setOperations = redisTemplate.opsForSet();
        setOperations.remove(Constant.LIMIT_URL_KEY, uri);
        String singleKey = Constant.LIMIT_SINGLE_KEY + uri;
        String wholeKey = Constant.LIMIT_WHOLE_KEY + uri;
        String wholeLimiterKey = Constant.LIMIT_WHOLE_LIMITER_KEY + uri;
        redisTemplate.delete(singleKey);
        redisTemplate.delete(wholeKey);
        redisTemplate.delete(wholeLimiterKey);
        Set<String> keys = redisTemplate.keys(singleKey + ":*");
        redisTemplate.delete(keys);
    }


    public void checkApiLimitKey(SysApiLimitDto sysApiLimitVo) {
        if (StringUtils.isBlank(sysApiLimitVo.getUri())) {
            throw new WlException("接口uri不能为空");
        }
        String id = sysApiLimitVo.getId();
        if (StringUtils.isNotBlank(id)) {
            SysApiLimit sysApiLimit = sysApiLimitMapper.selectById(id);
            if (sysApiLimit.getUri().equals(sysApiLimitVo.getUri())) {
                return;
            }
        }
        LambdaQueryWrapper<SysApiLimit> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysApiLimit::getUri, sysApiLimitVo.getUri());
        Long count = sysApiLimitMapper.selectCount(wrapper);
        if (count > 0) {
            throw new WlException("接口URI已存在,禁止重复添加");
        }
    }
}
