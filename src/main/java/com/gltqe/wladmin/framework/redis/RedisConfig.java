package com.gltqe.wladmin.framework.redis;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis 序列化配置
 *
 * @author gltqe
 * @date 2022/7/3 0:39
 **/

@Configuration
public class RedisConfig {
    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    @Primary
    public RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer redisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
//       FastJson2JsonRedisSerializer fastJson2JsonRedisSerializer = new FastJson2JsonRedisSerializer(Object.class);
        // 设置value的序列化规则和 key的序列化规则
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(redisSerializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(redisSerializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Bean
    public RedisMessageListenerContainer container() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        return container;
    }


    /**
     * 将lua脚本的内容加载出来放入到DefaultRedisScript
     * @return
     */
    @Bean(name = "limitScript")
    public DefaultRedisScript<Long> limitScript() {
        DefaultRedisScript<Long> defaultRedisScript = new DefaultRedisScript<>();
        // defaultRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/iplimiter.lua")));
        defaultRedisScript.setScriptText(limitScriptTextZSet());
        defaultRedisScript.setResultType(Long.class);
        return defaultRedisScript;
    }


    /**
     * 限流脚本
     *
     * @return
     */
    private String limitScriptTextZSet() {
        return "redis.replicate_commands();\n" +
                "local singleKey = KEYS[1]\n" +
                "local singleUserKey = KEYS[2]\n" +
                "local wholeKey = KEYS[3]\n" +
                "local wholeLimiterKey = KEYS[4]\n" +
                "local currentTimeMicro = redis.call('TIME')[1]..redis.call('TIME')[2]\n" +
                "if redis.call('EXISTS',singleKey)==1 then\n" +
                "\tlocal sizeSingle = tonumber(redis.call('hget',singleKey,'size'))\n" +
                "\tlocal timeSingle  = tonumber(redis.call('hget',singleKey,'time'))\n" +
                "\tlocal timeMicroSingle  = tonumber(redis.call('hget',singleKey,'timeMicro'))\n" +
                "\tlocal currentLenSingle  = redis.call('ZCARD',singleUserKey)\n" +
                "\tif  currentLenSingle < sizeSingle then\n" +
                "\t\tredis.call('ZADD',singleUserKey,currentTimeMicro,currentTimeMicro)\n" +
                "\t\tredis.call('expire',singleUserKey,timeSingle)\n" +
                "\telse\n" +
                "\t\tlocal effectiveTime = tonumber(currentTimeMicro) - tonumber(timeMicroSingle)\n" +
                "\t\tlocal effectiveNum = redis.call('ZCOUNT',singleUserKey,effectiveTime,currentTimeMicro)\n" +
                "\t\tif  effectiveNum < sizeSingle and redis.call('ZREMRANGEBYSCORE',singleUserKey,0,effectiveTime) > 0 then\n" +
                "\t\t\tredis.call('ZADD',singleUserKey,currentTimeMicro,currentTimeMicro)\n" +
                "\t\t\tredis.call('expire',singleUserKey,timeSingle)\n" +
                "\t\telse\n" +
                "\t\t\treturn -1\n" +
                "\t\tend\n" +
                "\tend\n" +
                "end\n" +
                "\n" +
                "if redis.call('EXISTS',wholeKey)==1 then\n" +
                "\tlocal sizeWhole = tonumber(redis.call('hget',wholeKey,'size'))\n" +
                "\tlocal timeWhole = tonumber(redis.call('hget',wholeKey,'time'))\n" +
                "\tlocal timeMicroWhole = tonumber(redis.call('hget',wholeKey,'timeMicro'))\n" +
                "\tlocal currentLenWhole = redis.call('ZCARD',wholeLimiterKey)\n" +
                "\tif currentLenWhole < sizeWhole then\n" +
                "\t\tredis.call('ZADD',wholeLimiterKey,currentTimeMicro,currentTimeMicro)\n" +
                "\t\tredis.call('expire',wholeLimiterKey,timeWhole)\n" +
                "\telse\n" +
                "\t\tlocal effectiveTimeWhole = tonumber(currentTimeMicro) - tonumber(timeMicroWhole)\n" +
                "\t\tlocal effectiveNumWhole = redis.call('ZCOUNT',wholeLimiterKey,effectiveTimeWhole,currentTimeMicro)\n" +
                "\t\tif  effectiveNumWhole < sizeWhole and redis.call('ZREMRANGEBYSCORE',wholeLimiterKey,0,effectiveTimeWhole) > 0 then\n" +
                "\t\t\tredis.call('ZADD',wholeLimiterKey,currentTimeMicro,currentTimeMicro)\n" +
                "\t\t\tredis.call('expire',wholeLimiterKey,timeWhole)\n" +
                "\t\telse\n" +
                "\t\t\treturn -2\n" +
                "\t\tend\n" +
                "\tend\n" +
                "end\n" +
                "return 0";
    }
    /**
     * 限流脚本
     * 弃用
     * @return
     */
    private String limitScriptText() {
        return "redis.replicate_commands();\n" +
                "local singleKey = KEYS[1]\n" +
                "local singleUserKey = KEYS[2]\n" +
                "local wholeKey = KEYS[3]\n" +
                "local wholeLimiterKey = KEYS[4]\n" +
                "local currentTime = redis.call('TIME')[1]\n" +
                "if redis.call('EXISTS',singleKey)==1 then\n" +
                "\tlocal maxSize = tonumber(redis.call('hget',singleKey,'max'))\n" +
                "\tlocal outTime = tonumber(redis.call('hget',singleKey,'outTime'))\n" +
                "\tif redis.call('EXISTS',singleUserKey)==1 then\n" +
                "\t\tif redis.call('llen',singleUserKey) < maxSize then\n" +
                "\t\t\tredis.call('rpush',singleUserKey,currentTime)\n" +
                "\t\t\tredis.call('expire',singleUserKey,outTime)\n" +
                "\t\telse\n" +
                "\t\t\tlocal oldest = redis.call('lpop',singleUserKey)\n" +
                "\t\t\tif currentTime-oldest > outTime then\n" +
                "\t\t\t\tredis.call('rpush',singleUserKey,currentTime)\n" +
                "\t\t\t\tredis.call('expire',singleUserKey,outTime)\n" +
                "\t\t\telse\n" +
                "\t\t\t\tredis.call('lpush',singleUserKey,oldest)\n" +
                "\t\t\t\treturn -1\n" +
                "\t\t\tend\n" +
                "\t\tend\t\t\n" +
                "\telse\n" +
                "\t\tredis.call('rpush',singleUserKey,currentTime)\n" +
                "\t\tredis.call('expire',singleUserKey,outTime)\n" +
                "\tend\n" +
                "end\t\n" +
                "if redis.call('EXISTS',wholeKey)==1 then\n" +
                "\tlocal maxSizeWhole = tonumber(redis.call('hget',wholeKey,'max'))\n" +
                "\tlocal outTimeWhole = tonumber(redis.call('hget',wholeKey,'outTime'))\n" +
                "\tif redis.call('EXISTS',wholeLimiterKey)==1 then\n" +
                "\t\tif redis.call('llen',wholeLimiterKey) < maxSizeWhole then\n" +
                "\t\t\tredis.call('rpush',wholeLimiterKey,currentTime)\n" +
                "\t\t\tredis.call('expire',wholeLimiterKey,outTimeWhole)\n" +
                "\t\telse\n" +
                "\t\t\tlocal oldest = redis.call('lpop',wholeLimiterKey)\n" +
                "\t\t\tif currentTime-oldest > outTimeWhole then\n" +
                "\t\t\t\tredis.call('rpush',wholeLimiterKey,currentTime)\n" +
                "\t\t\t\tredis.call('expire',wholeLimiterKey,outTimeWhole)\n" +
                "\t\t\telse\n" +
                "\t\t\t\tredis.call('lpush',wholeLimiterKey,oldest)\n" +
                "\t\t\t\treturn -2\n" +
                "\t\t\tend\n" +
                "\t\tend\t\t\n" +
                "\telse\n" +
                "\t\tredis.call('rpush',wholeLimiterKey,currentTime)\n" +
                "\t\tredis.call('expire',wholeLimiterKey,outTimeWhole)\n" +
                "\tend\n" +
                "end\n" +
                "return 0";
    }
}
