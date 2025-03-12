package com.siukatech.poc.react.backend.module.core.caching.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.util.Objects;


@Slf4j
@Configuration
@EnableCaching
@EnableRedisRepositories
@ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "redis")
public class RedisCachingConfig extends DefaultCachingConfig {

    @Value("${spring.cache.redis.time-to-live:10m}")
    private java.time.Duration timeToLive;

//    @Getter
//    @ConfigurationProperties(prefix = "spring.cache")
//    public static class RedisCacheProp {
//        private String host;
//        private int port;
//    }

    /**
     * RedisProperties are the configuration mapped to "spring.data.redis"
     * LettuceConnectionFactory can be set-up through this properties
     */
    @Bean
    public LettuceConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        redisStandaloneConfiguration.setPort(redisProperties.getPort());
        boolean hasPasswordProvided = Objects.nonNull(redisProperties.getPassword());
        if (hasPasswordProvided) {
            redisStandaloneConfiguration.setPassword(redisProperties.getPassword());
        }
        redisStandaloneConfiguration.setDatabase(redisProperties.getDatabase());
        if (Objects.nonNull(redisProperties.getLettuce())) {
            // do something
        }
        log.debug("redisConnectionFactory - redisStandaloneConfiguration: [${}]"
                        + ", redisProperties: [{}]"
                        + ", hasPasswordProvided: [{}]"
                        + ", redisProperties.getHost: [{}]"
                        + ", redisProperties.getPort: [{}]"
                        + ", redisProperties.getDatabase: [{}]"
                , redisStandaloneConfiguration
                , redisProperties
                , hasPasswordProvided
                , redisProperties.getHost()
                , redisProperties.getPort()
                , redisProperties.getDatabase()
        );
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    /**
     * The injection of LettuceConnectionFactory (RedisConnectionFactory) here
     * which requires the LettuceConnectionConfiguration to support
     */
    @Bean
    public RedisTemplate<?, ?> redisTemplate(
            RedisConnectionFactory connectionFactory
//            , ObjectMapper objectMapper
    ) {
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
//        template.setKeySerializer(new StringRedisSerializer());
////        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
//        template.setValueSerializer(new JdkSerializationRedisSerializer());
        return template;
    }

    /**
     * Reference:
     * https://docs.spring.io/spring-boot/reference/io/caching.html#io.caching.provider.redis
     * https://stackoverflow.com/a/52971347
     */
    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) -> {
            this.getCacheNameListWithDefaults().forEach(cacheName -> {
                log.debug("redisCacheManagerBuilderCustomizer - cacheName: [{}]", cacheName);
                builder.withCacheConfiguration(cacheName
                        , RedisCacheConfiguration
                                .defaultCacheConfig()
                                .entryTtl(timeToLive)
//                                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)))
                );
            });
        };
    }

    @Bean(name = "cacheManager")
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory);
        this.getCacheNameListWithDefaults().forEach(cacheName -> {
            log.debug("redisCacheManager - cacheName: [{}]", cacheName);
            builder.withCacheConfiguration(cacheName
                    , RedisCacheConfiguration
                            .defaultCacheConfig()
                            .entryTtl(this.timeToLive));
        });
        CacheManager cacheManager = builder.build();
        return cacheManager;
    }

}
