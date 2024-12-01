package com.gltqe.wladmin.commons.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author gltqe
 * @date 2024/11/10 10:41
 */
@Slf4j
public class CacheUtil {

    private static final RedisTemplate<String,Object> redisTemplate = SpringContextUtil.getBean(RedisTemplate.class);


}
