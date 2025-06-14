package com.gltqe.wladmin.system.config;

import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.gltqe.wladmin.commons.utils.RsaUtil;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author gltqe
 * @date 2024/11/1 9:51
 */
@Data
@Component
@ConfigurationProperties(prefix = "token")
public class JwtProperties {


    /**
     * accessToken过期时间 2小时
     */
    private Long accessTtl;

    /**
     * refreshToken过期时间 30天
     */
    private Long refreshTtl;

    /**
     * 加密类型
     */
    private String type;

    /**
     * 对称加密秘钥
     */
    private String secret;

    /**
     * 非对称加密秘钥的私钥
     */
    private String priSecret;

    /**
     * 非对称加密秘钥的公钥
     */
    private String pubSecret;

    /**
     * 对称秘钥
     */
    private byte[] secretByte;

    /**
     * 私钥
     */
    private PrivateKey pri;

    /**
     * 公钥
     */
    private PublicKey pub;

    @PostConstruct
    public void init() {

        if (StringUtils.isNotBlank(priSecret) && StringUtils.isNotBlank(pubSecret)) {
            pri = RsaUtil.string2PrivateKey(priSecret);
            pub = RsaUtil.string2PublicKey(pubSecret);
        }

        if (StringUtils.isNotBlank(secret)) {
            secretByte = secret.getBytes(StandardCharsets.UTF_8);
        }
    }

    public JWTSigner getSigner() {
        return switch (type) {
            case "hs256" -> JWTSignerUtil.hs256(secretByte);
            case "hs384" -> JWTSignerUtil.hs384(secretByte);
            case "hs512" -> JWTSignerUtil.hs512(secretByte);
            case "rs256" -> JWTSignerUtil.rs256(pri);
            case "rs384" -> JWTSignerUtil.rs384(pri);
            case "rs512" -> JWTSignerUtil.rs512(pri);
            default -> throw new RuntimeException("未匹配到签名方式");
        };
    }

    public JWTSigner getHs256Signer() {
        return JWTSignerUtil.hs256(secretByte);
    }

    public JWTSigner getHs384Signer() {
        return JWTSignerUtil.hs384(secretByte);
    }

    public JWTSigner getHs512Signer() {
        return JWTSignerUtil.hs512(secretByte);
    }

    public JWTSigner getRs256Signer() {
        return JWTSignerUtil.rs256(pri);
    }

    public JWTSigner getRs384Signer() {
        return JWTSignerUtil.rs384(pri);
    }

    public JWTSigner getRs512Signer() {
        return JWTSignerUtil.rs512(pri);
    }
}
