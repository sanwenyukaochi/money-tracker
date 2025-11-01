package org.secure.security.authentication.config;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.secure.security.authentication.handler.exception.CustomAuthenticationExceptionHandler;
import org.secure.security.authentication.handler.exception.CustomAuthorizationExceptionHandler;
import org.secure.security.authentication.handler.exception.CustomSecurityExceptionHandler;
import org.secure.security.authentication.handler.login.LoginFailHandler;
import org.secure.security.authentication.handler.login.LoginSuccessHandler;
import org.secure.security.authentication.handler.login.sms.SmsAuthenticationFilter;
import org.secure.security.authentication.handler.login.sms.SmsAuthenticationProvider;
import org.secure.security.authentication.handler.login.username.UsernameAuthenticationFilter;
import org.secure.security.authentication.handler.login.username.UsernameAuthenticationProvider;
import org.secure.security.authentication.filter.JwtTokenAuthenticationFilter;
import org.secure.security.authentication.handler.resourceapi.openapi2.OpenApi2AuthenticationFilter;
import org.secure.security.authentication.service.JwtService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class CustomWebSecurityConfig {

    private final ApplicationContext applicationContext;
    private final CustomAuthenticationExceptionHandler authenticationExceptionHandler;
    private final CustomAuthorizationExceptionHandler authorizationExceptionHandler;
    private final CustomSecurityExceptionHandler globalSpringSecurityExceptionHandler;

    /**
     * 禁用不必要的默认filter，处理异常响应内容
     */
    private void commonHttpSetting(HttpSecurity http) throws Exception {
        // 禁用SpringSecurity默认filter。这些filter都是非前后端分离项目的产物，用不上.
        // properties配置文件将日志设置DEBUG模式，就能看到加载了哪些filter
        // logging.level.org.springframework.security=DEBUG
        // 不disable。会默认创建默认filter

        // 前端段分离不需要---禁用默认登录页
        http.formLogin(AbstractHttpConfigurer::disable);
        // 前端段分离不需要---禁用明文验证
        http.httpBasic(AbstractHttpConfigurer::disable);
        // 前端段分离不需要---禁用退出页
        http.logout(AbstractHttpConfigurer::disable);
        // 前后端分离不需要---因为是无状态的
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // 前端段分离不需要---csrf攻击
        http.csrf(AbstractHttpConfigurer::disable);
        // 跨域访问权限，如果需要可以关闭后自己配置跨域访问
        http.cors(AbstractHttpConfigurer::disable);
        // requestCache用于重定向，前后端分析项目无需重定向，requestCache也用不上
        http.requestCache(cache -> cache.requestCache(new NullRequestCache()));
        // 前后端分离不需要---记住我
        http.rememberMe(AbstractHttpConfigurer::disable);
        // 无需给用户一个匿名身份
        http.anonymous(AbstractHttpConfigurer::disable);
        // 处理 SpringSecurity 异常响应结果。响应数据的结构，改成业务统一的JSON结构。不要框架默认的响应结构
        http.exceptionHandling(exceptionHandling -> {
            // 认证失败异常
            exceptionHandling.authenticationEntryPoint(authenticationExceptionHandler);
            // 鉴权失败异常
            exceptionHandling.accessDeniedHandler(authorizationExceptionHandler);
        });
        // 其他未知异常. 尽量提前加载。
        http.addFilterBefore(globalSpringSecurityExceptionHandler, SecurityContextHolderFilter.class);
    }

    /**
     * 密码加密使用的编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 登录api
     */
    @Bean
    public SecurityFilterChain loginFilterChain(HttpSecurity http) throws Exception {
        commonHttpSetting(http);
        // 使用securityMatcher限定当前配置作用的路径
        http.securityMatcher("/user/login/*")
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());

        LoginSuccessHandler loginSuccessHandler = applicationContext.getBean(LoginSuccessHandler.class);
        LoginFailHandler loginFailHandler = applicationContext.getBean(LoginFailHandler.class);

        // 加一个登录方式。用户名、密码登录
        UsernameAuthenticationFilter usernameAuthenticationFilter = new UsernameAuthenticationFilter(
                PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/user/login/username"),
                new ProviderManager(List.of(applicationContext.getBean(UsernameAuthenticationProvider.class))),
                loginSuccessHandler,
                loginFailHandler);

        http.addFilterBefore(usernameAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // 加一个登录方式。短信验证码 登录
        SmsAuthenticationFilter smsAuthenticationFilter = new SmsAuthenticationFilter(
                PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/user/login/sms"),
                new ProviderManager(List.of(applicationContext.getBean(SmsAuthenticationProvider.class))),
                loginSuccessHandler,
                loginFailHandler);

        http.addFilterBefore(smsAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public SecurityFilterChain myApiFilterChain(HttpSecurity http) throws Exception {
        // 使用securityMatcher限定当前配置作用的路径
        http.securityMatcher("/open-api/business-1")
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());
        commonHttpSetting(http);

        JwtTokenAuthenticationFilter openApi1Filter = new JwtTokenAuthenticationFilter(
                applicationContext.getBean(JwtService.class));
        // 加一个登录方式。用户名、密码登录
        http.addFilterBefore(openApi1Filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public SecurityFilterChain thirdApiFilterChain(HttpSecurity http) throws Exception {
        // 不使用securityMatcher限定当前配置作用的路径。所有没有匹配上指定SecurityFilterChain的请求，都走这里鉴权
        http.securityMatcher("/open-api/business-2")
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());
        commonHttpSetting(http);

        OpenApi2AuthenticationFilter openApiFilter = new OpenApi2AuthenticationFilter();
        // 加一个登录方式。用户名、密码登录
        http.addFilterBefore(openApiFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * 不鉴权的api
     */
    @Bean
    public SecurityFilterChain publicApiFilterChain(HttpSecurity http) throws Exception {
        commonHttpSetting(http);
        // 使用securityMatcher限定当前配置作用的路径
        http.securityMatcher("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**")
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        return http.build();
    }

//  /** 其余路径，走这个默认过滤链 */
//  @Bean
//  @Order(Integer.MAX_VALUE) // 这个过滤链最后加载
//  public SecurityFilterChain defaultApiFilterChain(HttpSecurity http) throws Exception {
//    commonHttpSetting(http);
//    http // 不用securityMatcher表示缺省值，匹配不上其他过滤链时，都走这个过滤链
//        .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());
//    http.addFilterBefore(new OpenApi3AuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//    return http.build();
//  }
}
