package com.siukatech.poc.react.backend.module.core.caching.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class RedisCachingHelper {

    public RedisSerializer<Object> resolveRedisSerializer(ObjectMapper objectMapper) {
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
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .allowCreateOnMissingCache(true)
                ;
//        cacheNameListWithDefaults.forEach(cacheName -> {
//            log.debug("resolveRedisCacheManager - cacheName: [{}]", cacheName);
//            builder.withCacheConfiguration(cacheName, redisCacheConfiguration);
//        });
//        RedisCacheManager redisCacheManager = builder.build();
//        return redisCacheManager;
        return builder;
    }

}
