package com.spring.security.authentication.handler.auth;

import tools.jackson.databind.json.JsonMapper;
import com.spring.security.common.web.constant.ResponseCodeConstants;
import com.spring.security.domain.model.dto.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * AbstractAuthenticationProcessingFilter抛出AuthenticationException异常后，会跑到这里来
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(@NonNull HttpServletRequest request, HttpServletResponse response,
                                        @NonNull AuthenticationException authenticationException) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        log.warn("登录异常：msg={}", authenticationException.getMessage(), authenticationException);
        JsonMapper.shared().writeValue(response.getOutputStream(), Result.error(ResponseCodeConstants.AUTH_LOGIN_FAILED, authenticationException.getMessage(), null));
    }
}
