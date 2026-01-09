package com.spring.security.common.cache;

import com.spring.security.authentication.handler.auth.UserLoginInfo;
import com.spring.security.common.cache.constant.RedisCache;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCache  {

    @CacheEvict(value = RedisCache.USER_INFO, key = "#username")
    public void evictUserLoginInfo(String username) {
    }

    @Cacheable(value = RedisCache.USER_INFO, key = "#username", unless = "#result == null")
    public UserLoginInfo getUserLoginInfo(String username) {
        return null;
    }

    @CachePut(value = RedisCache.USER_INFO, key = "#username")
    public UserLoginInfo putUserLoginInfo(String username, UserLoginInfo userLoginInfo) {
        return userLoginInfo;
    }

}
