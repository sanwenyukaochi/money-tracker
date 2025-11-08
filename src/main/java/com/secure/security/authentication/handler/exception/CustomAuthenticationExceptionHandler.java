package com.secure.security.authentication.handler.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secure.security.common.web.constant.ResponseCodeConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import com.secure.security.domain.model.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * 认证失败时，会执行这个方法。将失败原因告知客户端
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationExceptionHandler implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException e) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        if (!HttpMethod.POST.name().equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
            objectMapper.writeValue(response.getOutputStream(), Result.builder().code(ResponseCodeConstants.METHOD_NOT_ALLOWED).message("请求方法不支持" + request.getMethod()).build());
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            log.warn("登录异常：msg={}", e.getMessage(), e);
            objectMapper.writeValue(response.getOutputStream(), Result.builder().code(ResponseCodeConstants.LOGIN_FAIL).message("认证失败").build());
        }
    }
}
