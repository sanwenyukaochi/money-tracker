package com.secure.security.authentication.handler.auth.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secure.security.common.web.constant.ResponseCodeConstants;
import com.secure.security.common.web.exception.BaseException;
import com.secure.security.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.secure.security.domain.model.entity.User;
import com.secure.security.authentication.handler.auth.UserLoginInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 帐号密码登录认证
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UsernameAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 用户提交的用户名 + 密码：
        UsernameAuthentication usernameAuthentication = (UsernameAuthentication) authentication;
        String username = usernameAuthentication.getUsername();
        String password = usernameAuthentication.getPassword();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BaseException(ResponseCodeConstants.USER_NOT_FOUND, "用户不存在", HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            // 密码错误，直接抛异常。
            throw new BaseException(ResponseCodeConstants.PASSWORD_ERROR, "密码错误", HttpStatus.UNAUTHORIZED);
        }

        UserLoginInfo currentUser = objectMapper.convertValue(user, UserLoginInfo.class);//TODO 权限
        UsernameAuthentication token = new UsernameAuthentication(currentUser, true, List.of());
        // 认证通过，这里一定要设成true
        log.debug("用户名认证成功，用户: {}", currentUser.getUsername());
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernameAuthentication.class.isAssignableFrom(authentication);
    }
}
