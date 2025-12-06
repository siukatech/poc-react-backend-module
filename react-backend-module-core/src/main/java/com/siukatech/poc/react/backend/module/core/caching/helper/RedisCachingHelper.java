package com.siukatech.poc.react.backend.module.core.caching.helper;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class RedisCachingHelper {

    /**
     * https://stackoverflow.com/a/69520731
     *
     */
    public RedisSerializer<Object> resolveRedisSerializer(ObjectMapper objectMapper) {
        // Do not change the default object mapper, we need to serialize the class name into the value
        objectMapper = objectMapper.copy();
        objectMapper.registerModule(new CoreJackson2Module());
        // This method was deprecated in jackson 2.10 or higher use activateDefaultTyping instead of enableDefaultTyping
        objectMapper = objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator()
                , ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        //
        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }

    public <K, V> RedisTemplate<K, V> resolveRedisTemplate(
            RedisConnectionFactory redisConnectionFactory
            , ObjectMapper objectMapper
    ) {
        RedisTemplate<K, V> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
//        template.setKeySerializer(new StringRedisSerializer());
////        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
//        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.setDefaultSerializer(this.resolveRedisSerializer(objectMapper));
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        return template;
    }

    public RedisCacheManager.RedisCacheManagerBuilder resolveRedisCacheManagerBuilder(
            Duration timeToLive
            , List<String> cacheNameListWithDefaults
            , RedisConnectionFactory redisConnectionFactory
            , ObjectMapper objectMapper
    ) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(timeToLive)
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(this.resolveRedisSerializer(objectMapper)))
                ;
        RedisCacheManager.RedisCacheManagerBuilder redisCacheManagerBuilder = RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .allowCreateOnMissingCache(true)
                ;
//        cacheNameListWithDefaults.forEach(cacheName -> {
//            log.debug("resolveRedisCacheManager - cacheName: [{}]", cacheName);
//            redisCacheManagerBuilder.withCacheConfiguration(cacheName, redisCacheConfiguration);
//        });
//        RedisCacheManager redisCacheManager = redisCacheManagerBuilder.build();
//        return redisCacheManager;
        return redisCacheManagerBuilder;
    }

}
