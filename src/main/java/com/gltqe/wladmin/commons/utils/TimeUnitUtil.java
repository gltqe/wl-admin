package com.gltqe.wladmin.commons.utils;

import com.gltqe.wladmin.commons.enums.TimeUnitEnum;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 时间单位转换
 *
 * @author gltqe
 * @date 2022/7/5 9:43
 **/
public class TimeUnitUtil {
    public static final Long MICROSECOND = 1L;
    public static final Long MILLISECOND = 1000L;
    public static final Long SECOND = 1000L * 1000L;
    public static final Long MINUTE = 60 * SECOND;
    public static final Long HOUR = 60 * 60 * SECOND;
    public static final Long DAY = 24 * 60 * 60 * SECOND;
    public static final Integer SCALE = 2;

    /**
     * @param time        需要转换的时间数值
     * @param currentUnit 当前时间单位
     * @param targetUnit  目标时间单位
     * @param scale       保留小数位数
     * @return java.math.BigDecimal
     * @author gltqe
     * @date 2022/7/6 16:09
     **/
    public static BigDecimal convert(BigDecimal time, Long currentUnit, Long targetUnit, Integer scale) {
        if (scale == null) {
            scale = SCALE;
        }
        if (currentUnit < targetUnit) {
            BigDecimal b1 = new BigDecimal(targetUnit / currentUnit);
            return time.divide(b1, scale, RoundingMode.HALF_UP);
        } else if (currentUnit > targetUnit) {
            BigDecimal b1 = new BigDecimal(currentUnit / targetUnit);
            return time.multiply(b1).setScale(scale, RoundingMode.HALF_UP);
        } else {
            return time;
        }
    }

    public static BigDecimal convert(BigDecimal time, TimeUnitEnum current, TimeUnitEnum target, Integer scale) {
        return convert(time, current.getValue(), target.getValue(), scale);
    }
}
