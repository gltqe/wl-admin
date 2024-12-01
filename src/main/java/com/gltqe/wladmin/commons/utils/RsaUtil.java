package com.gltqe.wladmin.commons.utils;


import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gltqe
 * @date 2022/12/6 11:16
 **/
@Slf4j
public class RsaUtil {

    public static final String PRIVATE_KEY = "pri";

    public static final String PUBLIC_KEY = "pub";

    private static final String KEY_TYPE = "RSA";

    private static final Integer KEY_SIZE = 1024;

    /**
     * 生成秘钥对
     *
     * @return java.lang.String
     * @author gltqe
     * @date 2022/12/5 16:32
     **/
    public static KeyPair getKeyPair() {
        KeyPair keyPair = getKeyPair(KEY_SIZE);
        return keyPair;
    }

    public static KeyPair getKeyPair(Integer keySize) {
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(KEY_TYPE);
        } catch (NoSuchAlgorithmException e) {
            log.error("不支持算法:{}", KEY_TYPE);
            throw new RuntimeException(e);
        }
        keyPairGenerator.initialize(keySize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    public static Map<String, String> getKeyMap() {
        return getKeyMap(KEY_SIZE);
    }

    public static Map<String, String> getKeyMap(Integer keySize) {
        KeyPair keyPair = getKeyPair(keySize);
        String privateKey = getPrivateKey(keyPair);
        String publicKey = getPublicKey(keyPair);
        System.out.println("私钥:" + privateKey);
        System.out.println("公钥:" + publicKey);
        Map<String, String> map = new HashMap<>();
        map.put(PRIVATE_KEY, privateKey);
        map.put(PUBLIC_KEY, publicKey);
        return map;
    }

    /**
     * 获取公钥Base64字符串
     *
     * @param keyPair
     * @return java.lang.String
     * @author gltqe
     * @date 2022/12/5 16:32
     **/
    public static String getPublicKey(KeyPair keyPair) {
        PublicKey publicKey = keyPair.getPublic();
        byte[] bytes = publicKey.getEncoded();
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String getPublicKey(PublicKey publicKey) {
        byte[] bytes = publicKey.getEncoded();
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 获取私钥Base64字符串
     *
     * @param keyPair
     * @return java.lang.String
     * @author gltqe
     * @date 2022/12/5 16:32
     **/
    public static String getPrivateKey(KeyPair keyPair) {
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] bytes = privateKey.getEncoded();
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String getPrivateKey(PrivateKey privateKey) {
        byte[] bytes = privateKey.getEncoded();
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Base64编码后的公钥转换成PublicKey对象
     *
     * @param pubStr
     * @return java.lang.String
     * @author gltqe
     * @date 2022/12/5 16:32
     **/
    public static PublicKey string2PublicKey(String pubStr) {
        byte[] keyBytes = Base64.getDecoder().decode(pubStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_TYPE);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (Exception e) {
            log.error("转换为公钥类异常");
            throw new RuntimeException(e);
        }

    }

    /**
     * Base64编码后的私钥转换成PrivateKey对象
     *
     * @param priStr
     * @return java.lang.String
     * @author gltqe
     * @date 2022/12/5 16:32
     **/
    public static PrivateKey string2PrivateKey(String priStr) {
        byte[] keyBytes = Base64.getDecoder().decode(priStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_TYPE);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return privateKey;
        } catch (Exception e) {
            log.error("转换为私钥类异常");
            throw new RuntimeException(e);
        }

    }

    /**
     * 公钥加密
     *
     * @param content
     * @param publicKey
     * @return byte[]
     * @author gltqe
     * @date 2022/12/6 10:36
     **/
    public static byte[] publicEncrypt(byte[] content, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(KEY_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] bytes = cipher.doFinal(content);
            return bytes;
        } catch (Exception e) {
            log.error("公钥加密异常");
            throw new RuntimeException(e);
        }
    }

    public static String publicEncrypt(String data, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(KEY_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] bytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            log.error("公钥加密异常");
            throw new RuntimeException(e);
        }
    }

    public static String publicEncrypt(String data, String pubKey) {
        try {
            PublicKey publicKey = string2PublicKey(pubKey);
            Cipher cipher = Cipher.getInstance(KEY_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] bytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            log.error("公钥加密异常");
            throw new RuntimeException(e);
        }
    }

    /**
     * 私钥解密
     *
     * @param content
     * @param privateKey
     * @return byte[]
     * @author gltqe
     * @date 2022/12/6 10:36
     **/
    public static byte[] privateDecrypt(byte[] content, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(KEY_TYPE);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] bytes = cipher.doFinal(content);
            return bytes;
        } catch (Exception e) {
            log.error("私钥解密异常");
            throw new RuntimeException(e);
        }
    }

    public static String privateDecrypt(String data, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(KEY_TYPE);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(data));
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("私钥解密异常");
            throw new RuntimeException(e);
        }
    }

    public static String privateDecrypt(String data, String priKey) {
        try {
            PrivateKey privateKey = string2PrivateKey(priKey);
            Cipher cipher = Cipher.getInstance(KEY_TYPE);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(data));
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("私钥解密异常");
            throw new RuntimeException(e);
        }
    }
}
