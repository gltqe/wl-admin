package com.gltqe.wladmin.framework.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author gltqe
 * @date 2025/3/21 17:32
 */
@Data
@Component
@ConfigurationProperties(prefix = "cache")
public class CacheProperties {

    /**
     * 缓存类型 redis | local
     */
    private String type;

    /**
     * local默认过期时间 -1:永不过期
     */
    private long localDefaultTtl;

    /**
     * 过期key检测周期(秒)
     */
    private long localPeriod;
}
