package org.secure.security.authentication.handler.resourceapi.openapi1;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.secure.security.authentication.service.JwtService;
import org.secure.security.authentication.handler.login.UserLoginInfo;
import org.secure.security.common.web.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class MyJwtAuthenticationFilter extends OncePerRequestFilter {
  private JwtService jwtService;

  public MyJwtAuthenticationFilter(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    log.debug("Use OpenApi1AuthenticationFilter");

    String jwtToken = request.getHeader("Authorization");
    if (StringUtils.isEmpty(jwtToken)) {
        throw  new BaseException("miss.token", "JWT token is missing!", HttpStatus.BAD_REQUEST);
    }
    if (jwtToken.startsWith("Bearer ")) {
      jwtToken = jwtToken.substring(7);
    }


    try {
      UserLoginInfo userLoginInfo = jwtService.verifyJwt(jwtToken, UserLoginInfo.class);

      MyJwtAuthentication authentication = new MyJwtAuthentication();
      authentication.setJwtToken(jwtToken);
      authentication.setAuthenticated(true); // 设置true，认证通过。
      authentication.setCurrentUser(userLoginInfo);
      // 认证通过后，一定要设置到SecurityContextHolder里面去。
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }catch (ExpiredJwtException e) {
      // 转换异常，指定code，让前端知道时token过期，去调刷新token接口
        throw new BaseException("token.expired", "jwt过期", HttpStatus.UNAUTHORIZED);
    } catch (Exception e) {
        throw new BaseException("token.invalid", "jwt无效", HttpStatus.UNAUTHORIZED);
    }
    // 放行
    filterChain.doFilter(request, response);
  }
}
