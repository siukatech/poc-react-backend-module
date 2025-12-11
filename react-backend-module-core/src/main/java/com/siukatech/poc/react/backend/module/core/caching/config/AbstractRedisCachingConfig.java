package com.siukatech.poc.react.backend.module.core.caching.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siukatech.poc.react.backend.module.core.caching.handler.RedisCacheErrorHandler;
import com.siukatech.poc.react.backend.module.core.caching.helper.RedisCachingHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@Configuration
public abstract class AbstractRedisCachingConfig extends AbstractCachingConfig {

    protected final RedisConnectionFactory redisConnectionFactory;

    protected final ThreadPoolTaskExecutorBuilder threadPoolTaskExecutorBuilder;

    protected final TaskDecorator taskDecorator;

    public AbstractRedisCachingConfig(RedisConnectionFactory redisConnectionFactory
            , ThreadPoolTaskExecutorBuilder threadPoolTaskExecutorBuilder
            , TaskDecorator taskDecorator) {
        this.redisConnectionFactory = redisConnectionFactory;
        this.threadPoolTaskExecutorBuilder = threadPoolTaskExecutorBuilder;
        this.taskDecorator = taskDecorator;
    }

    // You can then build your executor using the configured builder:
    @Bean
    public ThreadPoolTaskExecutor redisPoolResetThreadPoolTaskExecutor(
            ThreadPoolTaskExecutorBuilder threadPoolTaskExecutorBuilder
            , TaskDecorator taskDecorator) {
        return threadPoolTaskExecutorBuilder
                .corePoolSize(2)
                .maxPoolSize(4)
                .queueCapacity(10)
                .threadNamePrefix("redis-pool-reset-task-")
                .taskDecorator(taskDecorator)
                .build();
    }

    /**
     * RedisProperties are the configuration mapped to "spring.data.redis"
     * LettuceConnectionFactory can be set-up through this properties
     */
//    @Bean
//    public LettuceConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
//        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
//        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
//        redisStandaloneConfiguration.setPort(redisProperties.getPort());
//        boolean hasPasswordProvided = Objects.nonNull(redisProperties.getPassword());
//        if (hasPasswordProvided) {
//            redisStandaloneConfiguration.setPassword(redisProperties.getPassword());
//        }
//        redisStandaloneConfiguration.setDatabase(redisProperties.getDatabase());
//        if (Objects.nonNull(redisProperties.getLettuce())) {
//            // do something
//        }
//        log.debug("redisConnectionFactory - redisStandaloneConfiguration: [${}]"
//                        + ", redisProperties: [{}]"
//                        + ", hasPasswordProvided: [{}]"
//                        + ", redisProperties.getHost: [{}]"
//                        + ", redisProperties.getPort: [{}]"
//                        + ", redisProperties.getDatabase: [{}]"
//                , redisStandaloneConfiguration
//                , redisProperties
//                , hasPasswordProvided
//                , redisProperties.getHost()
//                , redisProperties.getPort()
//                , redisProperties.getDatabase()
//        );
//        return new LettuceConnectionFactory(redisStandaloneConfiguration);
//    }

    /**
     * The injection of LettuceConnectionFactory (RedisConnectionFactory) here
     * which requires the LettuceConnectionConfiguration to support
     */
    @Bean
    public RedisTemplate<?, ?> redisTemplate(
            ObjectMapper objectMapper
//            , RedisConnectionFactory redisConnectionFactory
            , RedisCachingHelper redisCachingHelper
    ) {
//        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
//        template.setConnectionFactory(redisConnectionFactory);
////        template.setKeySerializer(new StringRedisSerializer());
//////        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
////        template.setValueSerializer(new JdkSerializationRedisSerializer());
        RedisTemplate<byte[], byte[]> template = redisCachingHelper
                .resolveRedisTemplate(redisConnectionFactory, objectMapper);
        template.afterPropertiesSet();
        log.debug("redisTemplate - template: [{}]", template);
        return template;
    }

    /**
     * Reference:
     * https://docs.spring.io/spring-boot/reference/io/caching.html#io.caching.provider.redis
     * https://stackoverflow.com/a/52971347
     */
//    @Bean
//    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
//        return (builder) -> {
//            this.getCacheNameListWithDefaults().forEach(cacheName -> {
//                log.debug("redisCacheManagerBuilderCustomizer - cacheName: [{}]", cacheName);
//                builder.withCacheConfiguration(cacheName
//                        , RedisCacheConfiguration
//                                .defaultCacheConfig()
//                                .entryTtl(timeToLive)
////                                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
////                                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)))
//                );
//            });
//        };
//    }

    protected RedisCacheErrorHandler resolveCacheErrorHandler() {
        return new RedisCacheErrorHandler(redisConnectionFactory
                , this.redisPoolResetThreadPoolTaskExecutor(threadPoolTaskExecutorBuilder, taskDecorator)
        );
    }

}
