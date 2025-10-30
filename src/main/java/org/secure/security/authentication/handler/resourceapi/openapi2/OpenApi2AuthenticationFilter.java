package org.secure.security.authentication.handler.resourceapi.openapi2;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.secure.security.common.web.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class OpenApi2AuthenticationFilter extends OncePerRequestFilter {

  public OpenApi2AuthenticationFilter() {
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
      log.debug("Use OpenApi2AuthenticationFilter...");

    String appId = request.getHeader("x-app-id");
    if (StringUtils.isEmpty(appId)) {
        throw  new BaseException("miss.appId", "x-app-id token is missing!", HttpStatus.BAD_REQUEST);
    }

    // 认证开始前，按SpringSecurity设计，要将Authentication设置到SecurityContext里面去。
    System.out.println("appId认证通过...");

    OpenApi2Authentication authentication = new OpenApi2Authentication();

    OpenApi2LoginInfo userLoginInfo = new OpenApi2LoginInfo();
    userLoginInfo.setAppId(appId);
    userLoginInfo.setMerchantName("三方系统商户名称");

    authentication.setAuthenticated(true); // 设置true，认证通过。
    authentication.setCurrentUser(userLoginInfo);
    SecurityContextHolder.getContext().setAuthentication(authentication); // 一定要设置到ThreadLocal

    // 放行
    filterChain.doFilter(request, response);
  }
}
