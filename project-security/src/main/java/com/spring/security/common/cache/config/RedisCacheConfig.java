package com.spring.security.common.cache.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.security.authentication.handler.auth.jwt.constant.JWTConstants;
import com.spring.security.common.cache.constant.RedisCache;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.time.Duration;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(RedisConnectionFactory factory) {
        RedisStandaloneConfiguration redisConfig = ((LettuceConnectionFactory) factory).getStandaloneConfiguration();
        Config redissonConfig = new Config();
        redissonConfig.setCodec(new JsonJacksonCodec(new ObjectMapper()));
        redissonConfig.setUsername(redisConfig.getUsername());
        redissonConfig.setPassword(new String(redisConfig.getPassword().get()));
        redissonConfig.useSingleServer()
                .setAddress("redis://%s:%d".formatted(redisConfig.getHostName(), redisConfig.getPort()))
                .setDatabase(redisConfig.getDatabase());
        return Redisson.create(redissonConfig);
    }

    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) {
        return new RedissonSpringCacheManager(redissonClient, Map.of(
                RedisCache.USER_INFO, new CacheConfig(
                        Duration.ofMinutes(JWTConstants.tokenExpiredTime).toMillis(),
                        Duration.ofMinutes(JWTConstants.tokenExpiredTime).toMillis()
                )
        ));
    }

}
