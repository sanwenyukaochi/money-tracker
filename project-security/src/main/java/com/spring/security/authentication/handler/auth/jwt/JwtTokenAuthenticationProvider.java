package com.spring.security.authentication.handler.auth.jwt;

import com.spring.security.authentication.handler.auth.UserLoginInfo;
import com.spring.security.authentication.handler.auth.jwt.dto.JwtTokenUserLoginInfo;
import com.spring.security.authentication.handler.auth.jwt.service.JwtService;
import com.spring.security.common.cache.UserCache;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
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
                        "仅支持JWT身份验证提供程序"));
        JwtTokenAuthenticationToken jwtTokenAuthenticationToken = (JwtTokenAuthenticationToken) authentication;
        // 获取用户提交的JWT
        String jwtToken = (jwtTokenAuthenticationToken.getJwtToken() == null ? "NONE_PROVIDED" : jwtTokenAuthenticationToken.getJwtToken());
        // 验证用户信息
        JwtTokenUserLoginInfo jwtTokenUserLoginInfo = additionalAuthenticationChecks((JwtTokenAuthenticationToken) authentication);
        // 查询用户信息
        UserLoginInfo userLoginInfo = retrieveUser(jwtTokenUserLoginInfo.username(), jwtTokenUserLoginInfo.sessionId(), jwtTokenAuthenticationToken);
        // 构造成功结果
        return createSuccessAuthentication(jwtTokenAuthenticationToken, userLoginInfo);
    }

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return JwtTokenAuthenticationToken.class.isAssignableFrom(authentication);
    }

    protected Authentication createSuccessAuthentication(Authentication authentication,
                                                         UserLoginInfo userLoginInfo) {
        JwtTokenAuthenticationToken result = new JwtTokenAuthenticationToken(userLoginInfo, List.of());
        // 认证通过，这里一定要设成true
        log.debug("JWT认证成功，用户: {}", userLoginInfo.getUsername());
        return result;
    }

    protected UserLoginInfo retrieveUser(String username, String sessionId, JwtTokenAuthenticationToken authentication) throws AuthenticationException {
        UserLoginInfo loadedUser = userCache.getUserLoginInfo(username, sessionId);
        log.debug("用户信息查询成功，用户: {}", loadedUser.getUsername());
        return loadedUser;
    }

    protected JwtTokenUserLoginInfo additionalAuthenticationChecks(JwtTokenAuthenticationToken authentication) throws AuthenticationException {
        String presentedJwtToken = authentication.getJwtToken();
        JwtTokenUserLoginInfo jwtTokenUserLoginInfo = jwtService.validateJwtToken(presentedJwtToken);
        if (jwtTokenUserLoginInfo.username().isEmpty() || jwtTokenUserLoginInfo.sessionId().isEmpty()) {
            log.debug("JWT验证失败，因为用户名或sessionId的值为空");
            throw new BadCredentialsException(this.messages
                    .getMessage("jwtTokenAuthenticationProvider.badCredentials", "错误的凭证"));
        }
        return jwtTokenUserLoginInfo;
    }
}