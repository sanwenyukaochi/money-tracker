package com.spring.security.common.cache.config;

import com.spring.security.authentication.handler.auth.jwt.constant.JWTConstants;
import com.spring.security.common.cache.constant.RedisCache;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(RedisConnectionFactory redisConnectionFactory) {
        RedisStandaloneConfiguration redisConfig = ((LettuceConnectionFactory) redisConnectionFactory).getStandaloneConfiguration();
        Config redissonConfig = new Config();
        redissonConfig.setCodec(new JsonJacksonCodec());
        redissonConfig.setUsername(redisConfig.getUsername());
        redissonConfig.setPassword(new String(redisConfig.getPassword().get()));
        redissonConfig.useSingleServer()
                .setAddress("redis://%s:%d".formatted(redisConfig.getHostName(), redisConfig.getPort()))
                .setDatabase(redisConfig.getDatabase());
        return Redisson.create(redissonConfig);
    }

//    @Bean("cacheManagerDeprecated")
//    @Deprecated
//    @ConditionalOnProperty(prefix = "app.cache", name = "enable-redisson-cache-manager", havingValue = "true", matchIfMissing = false)
//    public CacheManager cacheManagerDeprecated(RedissonClient redissonClient) {
//        Map<String, CacheConfig> config = Map.of(
//                RedisCache.USER_INFO, new CacheConfig(
//                        Duration.ofMinutes(JWTConstants.tokenExpiredTime).toMillis(),
//                        Duration.ofMinutes(JWTConstants.tokenExpiredTime).toMillis()
//                )
//        );
//        RedissonSpringCacheManager cacheManager = new RedissonSpringCacheManager(redissonClient, config);
//        cacheManager.setCodec(new JsonJacksonCodec());
//        cacheManager.setAllowNullValues(true);
//        //cacheManager.setCacheNames();
//        //cacheManager.setTransactionAware(true);
//        //cacheManager.setConfig();
//        return cacheManager;
//    }

    @Bean
    public GenericJacksonJsonRedisSerializer genericJacksonJsonRedisSerializer() {
        return GenericJacksonJsonRedisSerializer.builder()
                .enableDefaultTyping(BasicPolymorphicTypeValidator.builder().allowIfBaseType(Object.class).build())
                .enableSpringCacheNullValueSupport()
                .build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(genericJacksonJsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(genericJacksonJsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        //启用锁机制
        RedisCacheWriter cacheWriter = RedisCacheWriter.lockingRedisCacheWriter(redisConnectionFactory);
        //序列化配置
        RedisCacheConfiguration defaultCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(genericJacksonJsonRedisSerializer()))
                .disableCachingNullValues()
                .entryTtl(Duration.ofMinutes(RedisCache.DEFAULT_TTL));
        return RedisCacheManager.builder(cacheWriter)
                .cacheDefaults(defaultCacheConfiguration)
                .transactionAware()
                .withInitialCacheConfigurations(Collections.singletonMap(
                        RedisCache.USER_INFO, defaultCacheConfiguration.entryTtl(Duration.ofMinutes(JWTConstants.tokenExpiredTime))))
                .build();
    }

}
