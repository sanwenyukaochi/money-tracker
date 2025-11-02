package com.secure.security.authentication.handler.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import com.secure.security.domain.model.dto.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * AbstractAuthenticationProcessingFilter抛出AuthenticationException异常后，会跑到这里来
 */
@Component
@RequiredArgsConstructor
public class LoginFailHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = exception.getMessage();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        Result responseData = Result.builder()
                .data(null)
                .code("login.fail")
                .message(errorMessage)
                .build();
        objectMapper.writeValue(response.getOutputStream(), responseData);

    }
}
