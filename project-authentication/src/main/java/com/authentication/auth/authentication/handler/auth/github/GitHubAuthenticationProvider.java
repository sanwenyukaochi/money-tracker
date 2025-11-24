package com.authentication.auth.authentication.handler.auth.github;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.authentication.auth.authentication.handler.auth.github.dto.GitHubOAuthMeta;
import com.authentication.auth.common.web.constant.ResponseCodeConstants;
import com.authentication.auth.common.web.exception.BaseException;
import com.authentication.auth.domain.repository.UserIdentityRepository;
import com.authentication.auth.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.authentication.auth.authentication.handler.auth.UserLoginInfo;
import com.authentication.auth.authentication.handler.auth.github.service.GitHubOAuth2Service;
import com.authentication.auth.domain.model.entity.UserIdentity;
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
                objectMapper.convertValue(userRepository.findById(userIdentityOptional.get().getUserId()).orElseThrow(() -> new BaseException(ResponseCodeConstants.USER_NOT_FOUND, "用户不存在", HttpStatus.UNAUTHORIZED)), UserLoginInfo.class);//TODO 权限
        GitHubAuthenticationToken token = new GitHubAuthenticationToken(currentUser, true, List.of());
        token.setDetails(userIdentityOptional.isEmpty() ? new GitHubOAuthMeta(true, providerUserId) : null);
        // 构造认证对象
        log.debug("GitHub认证成功，用户: {}", currentUser.getUsername());
        return token;
    }

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return GitHubAuthenticationToken.class.isAssignableFrom(authentication);
    }
}