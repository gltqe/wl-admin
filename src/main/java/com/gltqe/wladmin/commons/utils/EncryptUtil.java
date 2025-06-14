package com.gltqe.wladmin.commons.utils;

import cn.hutool.core.util.HexUtil;

import java.security.SecureRandom;

/**
 * @author gltqe
 * @date 2024/9/16 22:03
 */
public class EncryptUtil {

    /**
     * 获取指定长度随机字节数组
     * @author gltqe
     * @date 2024/9/16 22:16
     * @param len
     * @return: byte[]
     */
    public static  byte[] getRandomByte(int len) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[len];
        random.nextBytes(key);
        return key;
    }
}
