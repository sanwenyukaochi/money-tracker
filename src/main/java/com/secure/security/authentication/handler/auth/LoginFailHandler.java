package com.secure.security.authentication.handler.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secure.security.common.web.constant.ResponseCodeConstants;
import com.secure.security.domain.model.dto.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * AbstractAuthenticationProcessingFilter抛出AuthenticationException异常后，会跑到这里来
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginFailHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        log.warn("登录异常：msg={}", e.getMessage(), e);
        objectMapper.writeValue(response.getOutputStream(), Result.builder()
                .data(null)
                .code(ResponseCodeConstants.LOGIN_FAIL)
                .message(e.getMessage())
                .build());

    }
}
