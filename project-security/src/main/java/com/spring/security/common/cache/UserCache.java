package com.spring.security.common.cache;

import com.spring.security.authentication.handler.auth.UserLoginInfo;
import com.spring.security.common.cache.constant.RedisCache;
import com.spring.security.common.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCache  {

    private final UserService userService;

    @Cacheable(value = RedisCache.USER_INFO, key = "#username")
    public UserLoginInfo getUserLoginInfo(String username, String sessionId, Long expiredTime){
        UserLoginInfo userLoginInfo = userService.loadUserByUsername(username);
        userLoginInfo.setSessionId(sessionId);
        userLoginInfo.setExpiredTime(expiredTime);
        return userLoginInfo;
    }

    @CacheEvict(value = RedisCache.USER_INFO, key = "#username")
    public void evictUserLoginInfo(String username) {
    }
}
