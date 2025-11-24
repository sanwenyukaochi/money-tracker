package com.secure.security.authentication.handler.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import com.secure.security.authentication.handler.auth.jwt.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT令牌认证过滤器
 * 职责：
 * 1. 从HTTP请求中提取JWT令牌
 * 2. 创建未认证的JwtTokenAuthentication对象
 * 3. 委托给AuthenticationManager进行实际认证
 * 4. 将认证结果设置到SecurityContext中
 * 设计理念：
 * - 遵循Spring Security的标准认证流程
 * - Filter只负责提取凭证，不做验证逻辑
 * - 认证逻辑交给AuthenticationProvider处理
 * - 异常处理交给Spring Security的异常处理机制
 */
@Slf4j
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public JwtTokenAuthenticationFilter(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.debug("开始JWT认证过滤器处理");

        // 1. 从请求中提取JWT令牌
        String jwtToken = jwtService.getJwtFromHeader(request);

        // 2. 如果没有JWT令牌，直接放行（可能是公开接口或其他认证方式）
        if (!StringUtils.hasText(jwtToken)) {
            log.debug("未找到JWT令牌，跳过JWT认证");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 3. 创建未认证的Authentication对象
            JwtTokenAuthenticationToken unauthenticatedToken = new JwtTokenAuthenticationToken(jwtToken, false);

            // 4. 委托给AuthenticationManager进行认证
            Authentication authenticatedToken = authenticationManager.authenticate(unauthenticatedToken);

            // 5. 认证成功，将结果设置到SecurityContext中
            SecurityContextHolder.getContext().setAuthentication(authenticatedToken);
            log.debug("JWT认证成功，用户已设置到SecurityContext");

        } catch (AuthenticationException e) {
            // 6. 认证失败，清空SecurityContext
            SecurityContextHolder.clearContext();
            log.debug("JWT认证失败: {}", e.getMessage());
            // 注意：这里不直接抛出异常，而是让Spring Security的异常处理机制处理
            // 异常会被ExceptionTranslationFilter捕获，然后调用AuthenticationEntryPoint
            throw e;
        }

        // 7. 继续过滤器链
        filterChain.doFilter(request, response);
    }
}
