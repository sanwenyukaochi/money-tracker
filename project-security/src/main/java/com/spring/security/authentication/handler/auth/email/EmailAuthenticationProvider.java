package com.spring.security.authentication.handler.auth.email;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.security.common.web.constant.ResponseCodeConstants;
import com.spring.security.common.web.exception.BaseException;
import lombok.RequiredArgsConstructor;
import com.spring.security.authentication.handler.auth.UserLoginInfo;
import com.spring.security.domain.model.entity.User;
import com.spring.security.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(@NonNull Authentication authentication) throws AuthenticationException {
        EmailAuthenticationToken emailAuthenticationToken = (EmailAuthenticationToken) authentication;
        String email = emailAuthenticationToken.getEmail();
        String password = emailAuthenticationToken.getPassword();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(ResponseCodeConstants.USER_EMAIL_NOT_FOUND, "邮箱不存在", HttpStatus.UNAUTHORIZED));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BaseException(ResponseCodeConstants.AUTH_PASSWORD_ERROR, "密码错误", HttpStatus.UNAUTHORIZED);
        }

        UserLoginInfo currentUser = new UserLoginInfo(null, user.getId(), user.getUsername(), user.getPassword(), user.getPhone(), user.getEmail(), user.getAccountNonLocked(), user.getAccountNonExpired(), user.getCredentialsNonExpired(), user.getEnabled(), user.getTwoFactorSecret(), user.getTwoFactorEnabled(), null);//TODO 权限
        EmailAuthenticationToken token = new EmailAuthenticationToken(currentUser, true, List.of());
        // 认证通过，这里一定要设成true
        log.debug("Email认证成功，用户: {}", currentUser.getUsername());
        return token;
    }

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return EmailAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
