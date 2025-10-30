package org.secure.security.authentication.handler.exception;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import lombok.extern.slf4j.Slf4j;
import org.secure.security.common.web.exception.BaseException;
import org.secure.security.common.web.util.JSON;
import org.secure.security.common.web.model.Result;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 捕捉Spring security filter chain 中抛出的未知异常
 */
@Slf4j
public class CustomSecurityExceptionHandler extends OncePerRequestFilter {

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (BaseException e) {
      // 自定义异常
      Result result = Result.builder()
          .message(e.getMessage())
          .code(e.getCode())
          .build();
      response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
      response.setStatus(e.getHttpStatus().value());
      PrintWriter writer = response.getWriter();
      writer.write(JSON.stringify(result));
      writer.flush();
      writer.close();
    } catch (AuthenticationException | AccessDeniedException e) {
      response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
      response.setStatus(HttpStatus.FORBIDDEN.value());
      PrintWriter writer = response.getWriter();
      writer.print(JSON.stringify(Result.fail(e.getMessage())));
      writer.flush();
      writer.close();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      // 未知异常
      Result result = Result.builder()
          .message("System Error")
          .code("system.error")
          .build();
      response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
      response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      PrintWriter writer = response.getWriter();
      writer.write(JSON.stringify(result));
      writer.flush();
      writer.close();
    }
  }
}
