package com.secure.security.security;

import com.secure.security.security.filter.AuthTokenFilter;
import com.secure.security.security.handler.SecurityAccessDeniedHandler;
import com.secure.security.security.handler.SecurityAuthenticationEntryPoint;
import com.secure.security.security.service.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    private final SecurityAuthenticationEntryPoint securityAuthenticationEntryPoint;

    private final SecurityAccessDeniedHandler securityAccessDeniedHandler;

    private final AuthTokenFilter authTokenFilter;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsServiceImpl);
        provider.setPasswordEncoder(passwordEncoder());
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // 前端段分离不需要---禁用明文验证
        http.httpBasic(AbstractHttpConfigurer::disable);
        // 前端段分离不需要---禁用默认登录页
        http.formLogin(AbstractHttpConfigurer::disable);
        // 前端段分离不需要---禁用退出页
        http.logout(AbstractHttpConfigurer::disable);
        // 前端段分离不需要---csrf攻击
        http.csrf(AbstractHttpConfigurer::disable);
        // 跨域访问权限，如果需要可以关闭后自己配置跨域访问
        http.cors(AbstractHttpConfigurer::disable);
        // 前后端分离不需要---因为是无状态的
        // http.sessionManagement(AbstractHttpConfigurer::disable);
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // 前后端分离不需要---记住我
        http.rememberMe(AbstractHttpConfigurer::disable);

        http.exceptionHandling(exception -> {
            // 请求未授权接口
            exception.authenticationEntryPoint(securityAuthenticationEntryPoint);
            // 没有权限访问
            exception.accessDeniedHandler(securityAccessDeniedHandler);
        });

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/api/auth/**"
                ).permitAll()
                .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/swagger-resources/**",
                        "/webjars/**"
                ).permitAll()
                .anyRequest().authenticated()
        );
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

