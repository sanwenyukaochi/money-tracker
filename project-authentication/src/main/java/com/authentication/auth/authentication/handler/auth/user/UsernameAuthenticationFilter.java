package com.authentication.auth.authentication.handler.auth.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * 用户名密码登录
 * AbstractAuthenticationProcessingFilter 的实现类要做的工作：
 * 1. 从HttpServletRequest提取授权凭证。假设用户使用 用户名/密码 登录，就需要在这里提取username和password。
 * 然后，把提取到的授权凭证封装到的Authentication对象，并且authentication.isAuthenticated()一定返回false
 * 2. 将Authentication对象传给AuthenticationManager进行实际的授权操作
 */
@Slf4j
public class UsernameAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final RequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = PathPatternRequestMatcher.withDefaults()
            .matcher(HttpMethod.POST, "/api/login/application/username");

    private final ObjectMapper objectMapper = new ObjectMapper();

    public UsernameAuthenticationFilter(AuthenticationManager authenticationManager,
                                        AuthenticationSuccessHandler authenticationSuccessHandler,
                                        AuthenticationFailureHandler authenticationFailureHandler) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        setAuthenticationSuccessHandler(authenticationSuccessHandler);
        setAuthenticationFailureHandler(authenticationFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                @NonNull HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.debug("use UsernameAuthenticationFilter");

        // 提取请求数据
        UsernameLoginRequest usernameLoginRequest = objectMapper.readValue(request.getInputStream(), UsernameLoginRequest.class);

        // 封装成Spring Security需要的对象
        UsernameAuthenticationToken authentication = new UsernameAuthenticationToken(usernameLoginRequest.username(), usernameLoginRequest.password(), false);
        // 开始登录认证。SpringSecurity会利用 Authentication对象去寻找 AuthenticationProvider进行登录认证
        return getAuthenticationManager().authenticate(authentication);
    }

}
