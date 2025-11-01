package org.secure.security.authentication.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.secure.security.authentication.handler.login.UserLoginInfo;
import org.secure.security.authentication.service.JwtService;
import org.secure.security.common.web.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.debug("Use OpenApi1AuthenticationFilter");

        String jwtToken = jwtService.getJwtFromHeader(request);

        try {
            UserLoginInfo userLoginInfo = jwtService.validateJwtToken(jwtToken, UserLoginInfo.class);
            JwtTokenAuthentication authentication = new JwtTokenAuthentication(jwtToken, userLoginInfo, true, List.of());
            // 认证通过后，一定要设置到SecurityContextHolder里面去。
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (MalformedJwtException e) {
            throw new BaseException("token.malformed", "JWT Token 无效", HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            throw new BaseException("token.expired", "JWT 已过期", HttpStatus.UNAUTHORIZED);
        } catch (UnsupportedJwtException e) {
            throw new BaseException("token.unsupported", "JWT 不受支持", HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e) {
            throw new BaseException("token.empty", "JWT 内容为空", HttpStatus.UNAUTHORIZED);
        } catch (JsonProcessingException e) {
            throw new BaseException("token.parse_error", "JWT 用户信息解析失败", HttpStatus.UNAUTHORIZED);
        }
        // 放行
        filterChain.doFilter(request, response);
    }
}
