package com.spring.security.authentication.handler.auth.jwt;

import com.spring.security.authentication.handler.auth.UserLoginInfo;
import com.spring.security.authentication.handler.auth.jwt.dto.JwtTokenUserLoginInfo;
import com.spring.security.authentication.handler.auth.jwt.service.JwtService;
import com.spring.security.authentication.handler.auth.message.SmsAuthenticationToken;
import com.spring.security.common.cache.UserCache;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * JWT认证提供者
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenAuthenticationProvider implements AuthenticationProvider {
    private final JwtService jwtService;
    private final UserCache userCache;
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Override
    @SneakyThrows
    public Authentication authenticate(@NonNull Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(JwtTokenAuthenticationToken.class, authentication,
                () -> this.messages.getMessage("JwtTokenAuthenticationProvider.onlySupports",
                        "Only JwtTokenAuthenticationToken is supported"));
        // JWT：
        JwtTokenAuthenticationToken jwtAuth = (JwtTokenAuthenticationToken) authentication;
        String jwtToken = (jwtAuth.getJwtToken() == null ? "NONE_PROVIDED" : jwtAuth.getJwtToken());

        // 验证JWT并提取用户信息
        JwtTokenUserLoginInfo jwtTokenUserLoginInfo = jwtService.validateJwtToken(jwtToken);
        // 获取用户信息
        UserLoginInfo userDetails = userCache.getUserLoginInfo(jwtTokenUserLoginInfo.username(), jwtTokenUserLoginInfo.sessionId());

        JwtTokenAuthenticationToken authenticatedToken = new JwtTokenAuthenticationToken(userDetails, List.of());
        // 认证通过，这里一定要设成true
        log.debug("JWT认证成功，用户: {}", jwtTokenUserLoginInfo.username());
        return authenticatedToken;

    }

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return JwtTokenAuthenticationToken.class.isAssignableFrom(authentication);
    }
}