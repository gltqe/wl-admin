package com.gltqe.wladmin.commons.utils;

import java.math.BigDecimal;

/**
 * 存储单位换算
 *
 * @author gltqe
 * @date 2022/7/3 0:31
 **/
public class StorageUnitUtil {

    public static final Long BIT = 1L;
    public static final Long BYTE = 8L;
    public static final Long KB = 8 * 1024L;
    public static final Long MB = 8 * 1024 * 1024L;
    public static final Long GB = 8 * 1024 * 1024 * 1024L;
    public static final Long TB = 8 * 1024 * 1024 * 1024 * 1024L;
    public static final Integer SCALE = 2;

    /**
     * 存储单位换算
     *
     * @param length      需要转换的存储大小
     * @param currentUnit 当前存储单位
     * @param targetUnit  转换目标存储单位
     * @param scale       小数点
     * @return java.math.BigDecimal
     * @author gltqe
     * @date 2022/7/3 0:32
     **/
    public static BigDecimal convert(BigDecimal length, Long currentUnit, Long targetUnit, Integer scale) {
        if (scale == null) {
            scale = SCALE;
        }
        if (currentUnit < targetUnit) {
            BigDecimal b1 = new BigDecimal(targetUnit / currentUnit);
            BigDecimal decimal = length.divide(b1, scale, BigDecimal.ROUND_HALF_UP);
            return decimal;
        } else if (currentUnit > targetUnit) {
            BigDecimal b1 = new BigDecimal(currentUnit / targetUnit);
            BigDecimal decimal = length.multiply(b1).setScale(scale, BigDecimal.ROUND_HALF_UP);
            return decimal;
        } else {
            return length;
        }
    }

}
