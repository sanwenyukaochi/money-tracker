package com.spring.security.authentication.handler.auth.message;

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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.UUID;

/**
 * 手机号验证码登录认证
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SmsAuthenticationProvider implements AuthenticationProvider {
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(@NonNull Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(SmsAuthenticationToken.class, authentication,
                () -> this.messages.getMessage("SmsAuthenticationProvider.onlySupports",
                        "仅支持手机号验证码身份验证提供程序"));
        SmsAuthenticationToken smsAuthenticationToken = (SmsAuthenticationToken) authentication;
        // 获取用户提交的手机号
        String phone = (smsAuthenticationToken.getPhone() == null ? "NONE_PROVIDED" : smsAuthenticationToken.getPhone());
        // 查询用户信息
        User user = retrieveUser(phone, smsAuthenticationToken);
        // 验证用户信息
        additionalAuthenticationChecks(user, (SmsAuthenticationToken) authentication);
        // 构造成功结果
        return createSuccessAuthentication(smsAuthenticationToken, user);
    }

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }

    protected Authentication createSuccessAuthentication(Authentication authentication,
                                                         User user) {
        UserLoginInfo userLoginInfo = JsonMapper.shared().convertValue(user, UserLoginInfo.class);
        userLoginInfo.setSessionId(UUID.randomUUID().toString());
        SmsAuthenticationToken result = new SmsAuthenticationToken(userLoginInfo, List.of());
        // 认证通过，这里一定要设成true
        log.debug("手机号认证成功，用户: {}", userLoginInfo.getUsername());
        return result;
    }

    protected User retrieveUser(String phone, SmsAuthenticationToken authentication) throws AuthenticationException {
        User loadedUser = userRepository.findByPhone(phone).orElseThrow(() -> new BaseException(ResponseCodeConstants.USER_PHONE_NOT_FOUND, "手机号不存在", HttpStatus.NOT_FOUND));
        log.debug("用户信息查询成功，用户: {}", loadedUser.getUsername());
        return loadedUser;
    }

    protected void additionalAuthenticationChecks(User user,
                                                  SmsAuthenticationToken authentication) throws AuthenticationException {
        String presentedSmsCode = authentication.getSmsCode();
        if (!presentedSmsCode.equals("000000")) {
            log.debug("身份验证失败，因为验证码与存储的值不匹配");
            throw new BadCredentialsException(this.messages
                    .getMessage("smsAuthenticationProvider.badCredentials", "错误的凭证"));
        }
    }
}
