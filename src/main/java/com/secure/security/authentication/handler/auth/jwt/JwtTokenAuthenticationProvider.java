package com.secure.security.authentication.handler.auth.jwt;

import com.secure.security.authentication.handler.auth.UserLoginInfo;
import com.secure.security.authentication.handler.auth.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * JWT认证提供者
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenAuthenticationProvider implements AuthenticationProvider {

    private final JwtService jwtService;

    @Override
    @SneakyThrows
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // JWT：
        JwtTokenAuthentication jwtAuth = (JwtTokenAuthentication) authentication;
        String jwtToken = jwtAuth.getJwtToken();

        // 验证JWT并提取用户信息
        UserLoginInfo currentUser = jwtService.validateJwtToken(jwtToken, UserLoginInfo.class);
        JwtTokenAuthentication authenticatedToken = new JwtTokenAuthentication(currentUser, true, List.of());
        // 认证通过，这里一定要设成true
        log.debug("JWT认证成功，用户: {}", currentUser.getUsername());
        return authenticatedToken;

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtTokenAuthentication.class.isAssignableFrom(authentication);
    }
}