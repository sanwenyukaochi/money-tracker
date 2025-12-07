package com.spring.security.authentication.handler.auth;

import com.spring.security.authentication.handler.auth.jwt.constant.JWTConstants;
import com.spring.security.authentication.handler.auth.jwt.dto.JwtTokenUserLoginInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.security.common.cache.UserCache;
import com.spring.security.common.web.constant.ResponseCodeConstants;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import com.spring.security.authentication.handler.auth.jwt.service.JwtService;
import com.spring.security.common.web.exception.BaseException;
import com.spring.security.domain.model.dto.Result;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * 认证成功/登录成功 事件处理器
 */
@Component
@RequiredArgsConstructor
public class LoginSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    private final ObjectMapper objectMapper;

    private final UserCache userCache;

    @PostConstruct
    public void disableRedirectStrategy() {
        setRedirectStrategy((_, _, _) -> {
            // 更改重定向策略，前后端分离项目，后端使用RestFul风格，无需做重定向
            // Do nothing, no redirects in REST
        });
    }

    @Override
    public void onAuthenticationSuccess(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserLoginInfo currentUser)) {
            throw new BaseException(ResponseCodeConstants.TYPE_ERROR, "登陆认证成功后，authentication.getPrincipal()返回的Object对象必须是：UserLoginInfo！", HttpStatus.BAD_REQUEST);
        }
        currentUser.setSessionId(UUID.randomUUID().toString());
        JwtTokenUserLoginInfo jwtTokenUserLoginInfo = new JwtTokenUserLoginInfo(currentUser.getSessionId(), currentUser.getUsername());
        // 生成token和refreshToken
        String token = generateToken(currentUser.getUsername(), jwtTokenUserLoginInfo);
        String refreshToken = generateRefreshToken(currentUser.getUsername(), jwtTokenUserLoginInfo);

        // 一些特殊的登录参数。比如三方登录，需要额外返回一个字段是否需要跳转的绑定已有账号页面
        @SuppressWarnings("unchecked")
        Map<String, Object> additionalInfo = Optional.ofNullable(authentication.getDetails())
                .filter(Map.class::isInstance)
                .map(Map.class::cast)
                .orElse(Map.of());

        boolean isNewUser = Boolean.FALSE.equals(additionalInfo.get("isNewUser")) || authentication.getDetails() == null;
        if (isNewUser) userCache.getUserLoginInfo(jwtTokenUserLoginInfo.getUsername(), jwtTokenUserLoginInfo.getSessionId(), jwtTokenUserLoginInfo.getExpiredTime());

        LoginResponse loginResponse = new LoginResponse(token, refreshToken, additionalInfo);
        // 虽然APPLICATION_JSON_UTF8_VALUE过时了，但也要用。因为Postman工具不声明utf-8编码就会出现乱码
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), Result.success("登录成功", loginResponse));
    }

    public String generateToken(String username, JwtTokenUserLoginInfo jwtTokenUserLoginInfo) {
        jwtTokenUserLoginInfo.setExpiredTime(JWTConstants.tokenExpiredTime);
        return jwtService.generateTokenFromUsername(username, jwtTokenUserLoginInfo, JWTConstants.tokenExpiredTime);
    }

    private String generateRefreshToken(String username, JwtTokenUserLoginInfo jwtTokenUserLoginInfo) {
        jwtTokenUserLoginInfo.setExpiredTime(JWTConstants.refreshTokenExpiredTime);
        return jwtService.generateTokenFromUsername(username, jwtTokenUserLoginInfo, JWTConstants.refreshTokenExpiredTime);
    }

}
