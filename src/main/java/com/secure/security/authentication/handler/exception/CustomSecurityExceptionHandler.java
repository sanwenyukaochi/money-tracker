package com.secure.security.authentication.handler.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secure.security.common.web.constant.ResponseCodeConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.secure.security.common.web.exception.BaseException;
import com.secure.security.domain.model.dto.Result;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 捕捉Spring security filter chain 中抛出的未知异常
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomSecurityExceptionHandler extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    public void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                 @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (BaseException e) {
            log.warn("认证异常：code={}, msg={}, status={}", e.getCode(), e.getMessage(), e.getHttpStatus(), e);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(e.getHttpStatus().value());
            objectMapper.writeValue(response.getOutputStream(), Result.builder()
                    .code(e.getCode())
                    .message(e.getMessage())
                    .build());
        } catch (AuthenticationException | AccessDeniedException e) {
            log.warn("Spring Security异常：msg={}", e.getMessage(), e);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            objectMapper.writeValue(response.getOutputStream(), Result.builder()
                    .code(ResponseCodeConstants.SYSTEM_ERROR)
                    .message(e.getMessage())
                    .build());
        } catch (Exception e) {
            log.warn("未知异常：msg={}",e.getMessage(), e);
            // 未知异常
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            objectMapper.writeValue(response.getOutputStream(), Result.builder()
                    .code(ResponseCodeConstants.SYSTEM_ERROR)
                    .message("未知异常")
                    .build());
        }
    }
}
