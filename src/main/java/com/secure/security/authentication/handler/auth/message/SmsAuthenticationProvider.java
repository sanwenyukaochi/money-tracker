package com.secure.security.authentication.handler.auth.message;

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
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmsAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;

    private final ObjectMapper objectMapper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 用户提交的手机号 + 验证码：
        SmsAuthentication smsAuthentication = (SmsAuthentication) authentication;
        String phone =  smsAuthentication.getPhone();
        String smsCode = smsAuthentication.getSmsCode();

        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new BaseException(ResponseCodeConstants.PHONE_NOT_FOUND, "手机号不存在", HttpStatus.UNAUTHORIZED));

        // 验证验证码是否正确
        if (!validateSmsCode(smsCode)) {
            throw new BaseException(ResponseCodeConstants.SMS_CODE_ERROR, "验证码错误", HttpStatus.UNAUTHORIZED);
        }

        UserLoginInfo currentUser = objectMapper.convertValue(user, UserLoginInfo.class);//TODO 权限
        SmsAuthentication token = new SmsAuthentication(currentUser, true, List.of());
        // 认证通过，一定要设成true
        log.debug("手机号认证成功，用户: {}", currentUser.getUsername());
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthentication.class.isAssignableFrom(authentication);
    }

    private boolean validateSmsCode(String smsCode) {
        // todo
        return smsCode.equals("000000");
    }
}
