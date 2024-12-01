package com.gltqe.wladmin;

import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.gltqe.wladmin.commons.utils.RsaUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gltqe
 * @date 2024/7/24 16:34
 */
@Slf4j
@SpringBootTest
public class JwtTest {

    @Test
    public void test() {
        Map<String, Object> map = new HashMap<>();

        byte[] jwtSecret = "123456".getBytes(StandardCharsets.UTF_8);
        String jwtSecretStr = Base64.getEncoder().encodeToString(jwtSecret);
        System.out.println("jwt的对称秘钥:" + jwtSecretStr);
        System.out.println("---------------对称秘钥测试(hs256)------------------");
        String token1 = JWTUtil.createToken(map, jwtSecret);
        boolean verify1 = JWTUtil.verify(token1, jwtSecret);
        System.out.println("token:" + token1);
        System.out.println("验签:" + verify1);
        System.out.println("---------------对称秘钥测试(hs384)------------------");
        String token2 = JWTUtil.createToken(map, JWTSignerUtil.hs384(jwtSecret));
        boolean verify2 = JWTUtil.verify(token2, JWTSignerUtil.hs384(jwtSecret));
        System.out.println("token:" + token2);
        System.out.println("验签:" + verify2);
        System.out.println("---------------对称秘钥测试(hs512)------------------");
        String token3 = JWTUtil.createToken(map, JWTSignerUtil.hs512(jwtSecret));
        boolean verify3 = JWTUtil.verify(token3, JWTSignerUtil.hs512(jwtSecret));
        System.out.println("token:" + token3);
        System.out.println("验签:" + verify3);

//        Map<String, String> keysMap = RsaUtil.getKeyMap();
//        String publicSecret1 = keysMap.get(RsaUtil.PUBLIC_KEY);
//        String privateSecret1 = keysMap.get(RsaUtil.PRIVATE_KEY);
//        PrivateKey privateKey = RsaUtil.string2PrivateKey(privateSecret1);
//        PublicKey publicKey = RsaUtil.string2PublicKey(publicSecret1);
        KeyPair keys = RsaUtil.getKeyPair();
        PublicKey publicKey = keys.getPublic();
        PrivateKey privateKey = keys.getPrivate();
        String publicSecret = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String privateSecret = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        System.out.println("jwt非对称秘钥公钥:" + publicSecret);
        System.out.println("jwt非对称秘钥私钥:" + privateSecret);
        System.out.println("---------------非对称秘钥测试(rs256)------------------");
        String token4 = JWTUtil.createToken(map, JWTSignerUtil.rs256(privateKey));
        boolean verify4 = JWTUtil.verify(token4, JWTSignerUtil.rs256(publicKey));
        System.out.println("token:" + token4);
        System.out.println("验签:" + verify4);
        System.out.println("---------------非对称秘钥测试(rs384)------------------");
        String token5 = JWTUtil.createToken(map, JWTSignerUtil.rs384(privateKey));
        boolean verify5 = JWTUtil.verify(token5, JWTSignerUtil.rs384(publicKey));
        System.out.println("token:" + token5);
        System.out.println("验签:" + verify5);
        System.out.println("---------------非对称秘钥测试(rs512)------------------");
        String token6 = JWTUtil.createToken(map, JWTSignerUtil.rs512(privateKey));
        boolean verify6 = JWTUtil.verify(token6, JWTSignerUtil.rs512(publicKey));
        System.out.println("token:" + token6);
        System.out.println("验签:" + verify6);
    }
}
