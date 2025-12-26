package com.spring.security.authentication.handler.auth.github;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.util.Assert;
import tools.jackson.databind.json.JsonMapper;
import com.spring.security.authentication.handler.auth.github.dto.GitHubOAuthMeta;
import com.spring.security.common.web.constant.ResponseCodeConstants;
import com.spring.security.common.web.exception.BaseException;
import com.spring.security.domain.repository.UserIdentityRepository;
import com.spring.security.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.spring.security.authentication.handler.auth.UserLoginInfo;
import com.spring.security.authentication.handler.auth.github.service.GitHubOAuth2Service;
import com.spring.security.domain.model.entity.UserIdentity;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class GitHubAuthenticationProvider implements AuthenticationProvider {
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private final UserIdentityRepository userIdentityRepository;
    private final GitHubOAuth2Service githubOAuth2Service;
    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(@NonNull Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(GitHubAuthenticationToken.class, authentication,
                () -> this.messages.getMessage("GitHubAuthenticationProvider.onlySupports",
                        "仅支持GitHub身份验证提供程序"));
        GitHubAuthenticationToken gitHubAuthenticationToken = (GitHubAuthenticationToken) authentication;
        // 获取用户提交的手机号
        String code = (gitHubAuthenticationToken.getCode() == null ? "NONE_PROVIDED" : gitHubAuthenticationToken.getCode());
        // 查询用户信息
        UserLoginInfo userLoginInfo = retrieveUser(code, gitHubAuthenticationToken);
        // 验证用户信息
        additionalAuthenticationChecks(userLoginInfo, (GitHubAuthenticationToken) authentication);
        // 构造成功结果
        return createSuccessAuthentication(gitHubAuthenticationToken, userLoginInfo);
    }

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return GitHubAuthenticationToken.class.isAssignableFrom(authentication);
    }

    protected Authentication createSuccessAuthentication(Authentication authentication,
                                                         UserLoginInfo userLoginInfo) {
        userLoginInfo.setSessionId(UUID.randomUUID().toString());
        GitHubAuthenticationToken result = new GitHubAuthenticationToken(userLoginInfo, List.of());
        // 必须转化成Map
        result.setDetails(JsonMapper.shared().convertValue(authentication.getDetails(), Map.class));
        // 认证通过，这里一定要设成true
        authentication.setAuthenticated(true);
        log.debug("GitHub认证成功，用户: {}", userLoginInfo.getUsername());
        return result;
    }

    protected UserLoginInfo retrieveUser(String code, GitHubAuthenticationToken authentication) throws AuthenticationException {
        OAuth2User oAuth2User = githubOAuth2Service.authenticateByCode(code);
        Long providerUserId = Optional.ofNullable(oAuth2User.getAttribute("id"))
                .filter(Number.class::isInstance)
                .map(Number.class::cast)
                .map(Number::longValue)
                .orElseThrow(() -> new BaseException(ResponseCodeConstants.USER_NOT_FOUND, "GitHub 用户 ID 无效或为空", HttpStatus.UNAUTHORIZED));
        UserLoginInfo loadedUser = userIdentityRepository
                .findOptionalByProviderUserIdAndProvider(providerUserId, UserIdentity.AuthProvider.GITHUB)
                .flatMap(identity -> userRepository.findById(identity.getUser().getId()))
                .map(user -> JsonMapper.shared().convertValue(user, UserLoginInfo.class))
                .orElseGet(() -> UserLoginInfo.builder().username(oAuth2User.getAttribute("login")).build());
        boolean isNewUser = loadedUser.getId() == null;
        authentication.setDetails(new GitHubOAuthMeta(isNewUser, providerUserId));
        log.debug("用户信息查询{}，用户: {}", !isNewUser ? "成功" : "失败", loadedUser.getUsername());
        return loadedUser;
    }

    protected void additionalAuthenticationChecks(UserLoginInfo userLoginInfo, GitHubAuthenticationToken authentication) throws AuthenticationException {
        String presentedCode = authentication.getCode();
        if (presentedCode == null || userLoginInfo == null) {
            log.debug("身份验证失败，因为身份与数据库存储的值不匹配");
            throw new BadCredentialsException(this.messages
            .getMessage("gitHubAuthenticationProvider.sessionExpired", "错误的凭证"));
        }
    }
}