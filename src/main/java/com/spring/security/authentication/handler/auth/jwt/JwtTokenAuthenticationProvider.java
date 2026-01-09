package com.spring.security.authentication.handler.auth.jwt;

import com.spring.security.authentication.handler.auth.UserLoginInfo;
import com.spring.security.authentication.handler.auth.jwt.dto.JwtTokenUserLoginInfo;
import com.spring.security.authentication.handler.auth.jwt.service.JwtService;
import com.spring.security.common.cache.UserCache;
import com.spring.security.domain.model.entity.User;
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
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Map;

/**
 * JWT认证提供者
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenAuthenticationProvider implements AuthenticationProvider {
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private final JwtService jwtService;
    private final UserCache userCache;

    @Override
    @SneakyThrows
    public Authentication authenticate(@NonNull Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(JwtTokenAuthenticationToken.class, authentication,
                () -> this.messages.getMessage("JwtTokenAuthenticationProvider.onlySupports",
                        "仅支持JWT身份验证提供程序"));
        JwtTokenAuthenticationToken jwtTokenAuthenticationToken = (JwtTokenAuthenticationToken) authentication;
        // 获取用户提交的JWT
        String jwtToken = (jwtTokenAuthenticationToken.getJwtToken() == null ? "NONE_PROVIDED" : jwtTokenAuthenticationToken.getJwtToken());
        // 查询用户信息
        User user = retrieveUser(jwtToken, jwtTokenAuthenticationToken);
        // 验证用户信息
        additionalAuthenticationChecks(user, (JwtTokenAuthenticationToken) authentication);
        // 构造成功结果
        return createSuccessAuthentication(jwtTokenAuthenticationToken, user);
    }

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return JwtTokenAuthenticationToken.class.isAssignableFrom(authentication);
    }

    protected Authentication createSuccessAuthentication(Authentication authentication,
                                                         User user) {
        UserLoginInfo userLoginInfo = userCache.getUserLoginInfo(user.getUsername());
        // 认证通过，使用 Authenticated 为 true 的构造函数
        JwtTokenAuthenticationToken result = new JwtTokenAuthenticationToken(userLoginInfo, List.of());
        // 必须转化成Map
        result.setDetails(JsonMapper.shared().convertValue(authentication.getDetails(), Map.class));
        log.debug("JWT认证成功，用户: {}", userLoginInfo.getUsername());
        return result;
    }

    protected User retrieveUser(String jwtToken, JwtTokenAuthenticationToken authentication) throws AuthenticationException {
        JwtTokenUserLoginInfo jwtTokenUserLoginInfo = jwtService.validateJwtToken(jwtToken);
        User loadedUser = new User();
        loadedUser.setUsername(jwtTokenUserLoginInfo.username());
        authentication.setDetails(null);
        log.debug("用户信息解析成功，用户: {}", jwtTokenUserLoginInfo.username());
        return loadedUser;
    }

    protected void additionalAuthenticationChecks(User user, JwtTokenAuthenticationToken authentication) throws AuthenticationException {
        String presentedJwtToken = authentication.getJwtToken();
        if (presentedJwtToken == null || user == null) {
            log.debug("身份验证失败，因为身份与存储的值不匹配");
            throw new BadCredentialsException(this.messages
                    .getMessage("jwtTokenAuthenticationProvider.sessionExpired", "错误的凭证"));
        }
    }
}