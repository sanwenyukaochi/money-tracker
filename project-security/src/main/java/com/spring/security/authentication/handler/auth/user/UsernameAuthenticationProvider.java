package com.spring.security.authentication.handler.auth.user;

import com.spring.security.common.web.constant.ResponseCodeConstants;
import com.spring.security.common.web.exception.BaseException;
import com.spring.security.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.spring.security.domain.model.entity.User;
import com.spring.security.authentication.handler.auth.UserLoginInfo;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Override
    public Authentication authenticate(@NonNull Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(UsernameAuthenticationToken.class, authentication,
                () -> this.messages.getMessage("UsernameAuthenticationProvider.onlySupports",
                        "Only UsernameAuthenticationToken is supported"));
        // 用户提交的用户名 + 密码：
        UsernameAuthenticationToken usernameAuthenticationToken = (UsernameAuthenticationToken) authentication;
        String username = (usernameAuthenticationToken.getUsername() == null ? "NONE_PROVIDED" : usernameAuthenticationToken.getUsername());
        String password = (usernameAuthenticationToken.getPassword() == null ? "NONE_PROVIDED" : usernameAuthenticationToken.getPassword());

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BaseException(ResponseCodeConstants.USER_NOT_FOUND, "用户不存在", HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            // 密码错误，直接抛异常。
            throw new BaseException(ResponseCodeConstants.AUTH_PASSWORD_ERROR, "密码错误", HttpStatus.UNAUTHORIZED);
        }

        UserLoginInfo currentUser = new UserLoginInfo();
        currentUser.setUsername(user.getUsername());
        UsernameAuthenticationToken token = new UsernameAuthenticationToken(currentUser, List.of());
        // 认证通过，这里一定要设成true
        log.debug("用户名认证成功，用户: {}", currentUser.getUsername());
        return token;
    }

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return UsernameAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
