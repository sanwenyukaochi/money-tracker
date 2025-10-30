package org.secure.security.authentication.handler.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.secure.security.common.web.model.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * 认证成功(Authentication), 但无权访问时。会执行这个方法
 * 或者SpringSecurity框架捕捉到  AccessDeniedException时，会转出
 */
public class CustomAuthorizationExceptionHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpStatus.FORBIDDEN.value());
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.writeValue(response.getOutputStream(), Result.error("${low.power:无权访问}"));
  }
}
