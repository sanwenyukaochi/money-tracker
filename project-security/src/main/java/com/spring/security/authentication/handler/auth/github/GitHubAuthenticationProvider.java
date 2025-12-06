package com.spring.security.authentication.handler.auth.github;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.security.authentication.handler.auth.github.dto.GitHubOAuthMeta;
import com.spring.security.common.web.constant.ResponseCodeConstants;
import com.spring.security.common.web.exception.BaseException;
import com.spring.security.domain.model.entity.User;
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

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GitHubAuthenticationProvider implements AuthenticationProvider {

    private final GitHubOAuth2Service githubOAuth2Service;

    private final UserRepository userRepository;

    private final UserIdentityRepository userIdentityRepository;

    private final ObjectMapper objectMapper;

    @Override
    public Authentication authenticate(@NonNull Authentication authentication) throws AuthenticationException {
        GitHubAuthenticationToken gitHubAuthenticationToken = (GitHubAuthenticationToken) authentication;
        String code = gitHubAuthenticationToken.getCode();
        OAuth2User oAuth2User = githubOAuth2Service.authenticateByCode(code);
        Long providerUserId = Optional.ofNullable(oAuth2User.getAttribute("id"))
                .filter(Number.class::isInstance)
                .map(Number.class::cast)
                .map(Number::longValue)
                .orElseThrow(() -> new BaseException(ResponseCodeConstants.USER_NOT_FOUND, "GitHub 用户 ID 无效或为空", HttpStatus.UNAUTHORIZED));

        Optional<UserIdentity> userIdentityOptional = userIdentityRepository.findOptionalByProviderUserIdAndProvider(providerUserId, UserIdentity.AuthProvider.GITHUB);
        UserLoginInfo currentUser = userIdentityOptional.isEmpty() ? UserLoginInfo.builder().username(oAuth2User.getAttribute("login")).build() :
                objectMapper.convertValue(userRepository.findById(userIdentityOptional.get().getUser().getId()).orElseThrow(() -> new BaseException(ResponseCodeConstants.USER_NOT_FOUND, "用户不存在", HttpStatus.UNAUTHORIZED)), UserLoginInfo.class);//TODO 权限
        GitHubAuthenticationToken token = new GitHubAuthenticationToken(currentUser, true, List.of());
        token.setDetails(new GitHubOAuthMeta(userIdentityOptional.isEmpty(), providerUserId));
        // 构造认证对象
        log.debug("GitHub认证成功，用户: {}", currentUser.getUsername());
        return token;
    }

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return GitHubAuthenticationToken.class.isAssignableFrom(authentication);
    }
}