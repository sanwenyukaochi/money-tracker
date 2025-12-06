package com.spring.security.authentication.handler.auth.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.security.common.web.constant.ResponseCodeConstants;
import com.spring.security.common.web.exception.BaseException;
import com.spring.security.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.spring.security.domain.model.entity.User;
import com.spring.security.authentication.handler.auth.UserLoginInfo;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmsAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(@NonNull Authentication authentication) throws AuthenticationException {
        // 用户提交的手机号 + 验证码：
        SmsAuthenticationToken smsAuthenticationToken = (SmsAuthenticationToken) authentication;
        String phone =  smsAuthenticationToken.getPhone();
        String smsCode = smsAuthenticationToken.getSmsCode();

        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new BaseException(ResponseCodeConstants.USER_PHONE_NOT_FOUND, "手机号不存在", HttpStatus.UNAUTHORIZED));

        // 验证验证码是否正确
        if (!validateSmsCode(smsCode)) {
            throw new BaseException(ResponseCodeConstants.AUTH_SMS_CODE_ERROR, "验证码错误", HttpStatus.UNAUTHORIZED);
        }

        UserLoginInfo currentUser = new UserLoginInfo(null, user.getId(), user.getUsername(), user.getPassword(), user.getPhone(), user.getEmail(), user.getAccountNonLocked(), user.getAccountNonExpired(), user.getCredentialsNonExpired(), user.getEnabled(), user.getTwoFactorSecret(), user.getTwoFactorEnabled(), null);//TODO 权限
        SmsAuthenticationToken token = new SmsAuthenticationToken(currentUser, true, List.of());
        // 认证通过，一定要设成true
        log.debug("手机号认证成功，用户: {}", currentUser.getUsername());
        return token;
    }

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private boolean validateSmsCode(String smsCode) {
        // todo
        return smsCode.equals("000000");
    }
}
