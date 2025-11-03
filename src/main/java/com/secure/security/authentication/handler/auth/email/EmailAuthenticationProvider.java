package com.secure.security.authentication.handler.auth.email;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secure.security.common.web.constant.ResponseCodeConstants;
import com.secure.security.common.web.exception.BaseException;
import lombok.RequiredArgsConstructor;
import com.secure.security.authentication.handler.auth.UserLoginInfo;
import com.secure.security.domain.model.entity.User;
import com.secure.security.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
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
    private final ObjectMapper objectMapper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        EmailAuthentication emailAuthentication = (EmailAuthentication) authentication;
        String email = emailAuthentication.getEmail();
        String password = emailAuthentication.getPassword();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(ResponseCodeConstants.EMAIL_NOT_FOUND, "邮箱不存在", HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BaseException(ResponseCodeConstants.PASSWORD_ERROR, "密码错误", HttpStatus.UNAUTHORIZED);
        }

        UserLoginInfo currentUser = objectMapper.convertValue(user, UserLoginInfo.class);//TODO 权限
        EmailAuthentication token = new EmailAuthentication(currentUser, true, List.of());
        // 认证通过，这里一定要设成true
        log.debug("Email认证成功，用户: {}", currentUser.getUsername());
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return EmailAuthentication.class.isAssignableFrom(authentication);
    }
}
