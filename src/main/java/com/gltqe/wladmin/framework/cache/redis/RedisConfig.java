package com.gltqe.wladmin.framework.cache.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
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
@ConditionalOnProperty(prefix = "cache",value = "type", havingValue = "redis")
public class RedisConfig {

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    @Bean("redisTemplate")
    @Primary
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);


        JsonMapper objectMapper = new JsonMapper();
        // 指定要序列化的域，field，get和set，以及修饰符范围。ANY指包括private和public修饰符范围
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 忽略JSON中存在但Java类中不匹配的字段
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 指定序列化输入类型，类的信息也将添加到json中
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);

        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer<Object> redisSerializer = new Jackson2JsonRedisSerializer<>(objectMapper,Object.class);
//        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
//       FastJson2JsonRedisSerializer<Object> redisSerializer = new FastJson2JsonRedisSerializer<>(Object.class);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // 设置value的序列化规则和 key的序列化规则
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(redisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(redisSerializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }


    @Bean("stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        stringRedisTemplate.setKeySerializer(stringRedisSerializer);
        stringRedisTemplate.setValueSerializer(stringRedisSerializer);
        stringRedisTemplate.setHashKeySerializer(stringRedisSerializer);
        stringRedisTemplate.setHashValueSerializer(stringRedisSerializer);
        stringRedisTemplate.afterPropertiesSet();
        return stringRedisTemplate;
    }

    @Bean
    public RedisMessageListenerContainer container() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        return container;
    }


    /**
     * 将lua脚本的内容加载出来放入到DefaultRedisScript
     *
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

}
