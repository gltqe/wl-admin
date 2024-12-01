package com.gltqe.wladmin;

import cn.hutool.core.util.HexUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

/**
 * @author gltqe
 * @date 2024/9/16 21:25
 */
@Slf4j
@SpringBootTest
public class PasswordTest {


    @Test
    public void testPassword() {
        String password = "123";
        String salt = getSalt();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println("原密码:" + password);
        System.out.println("盐:" + salt);
        System.out.println("加密后:" + bCryptPasswordEncoder.encode(password + salt));
    }


    private String getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        return HexUtil.encodeHexStr(key);
    }
}
