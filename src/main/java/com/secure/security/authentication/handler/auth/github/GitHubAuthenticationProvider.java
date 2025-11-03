package com.secure.security.authentication.handler.auth.github;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secure.security.common.web.constant.ResponseCodeConstants;
import com.secure.security.common.web.exception.BaseException;
import com.secure.security.domain.repository.UserIdentityRepository;
import com.secure.security.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.secure.security.authentication.handler.auth.UserLoginInfo;
import com.secure.security.authentication.handler.auth.github.service.GitHubOAuth2Service;
import com.secure.security.domain.model.entity.User;
import com.secure.security.domain.model.entity.UserIdentity;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GitHubAuthenticationProvider implements AuthenticationProvider {

    private final GitHubOAuth2Service githubOAuth2Service;

    private final UserRepository userRepository;

    private final UserIdentityRepository userIdentityRepository;

    private final ObjectMapper objectMapper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String code = (String) authentication.getPrincipal();
        try {
            OAuth2User oAuth2User = githubOAuth2Service.authenticateByCode(code);
            Long providerUserId = Optional.ofNullable(oAuth2User.getAttribute("id"))
                    .filter(Number.class::isInstance)
                    .map(Number.class::cast)
                    .map(Number::longValue)
                    .orElseThrow(() -> new BaseException(ResponseCodeConstants.USER_NOT_FOUND, "GitHub 用户 ID 无效或为空", HttpStatus.UNAUTHORIZED));

            UserIdentity userIdentity = userIdentityRepository.findByProviderUserIdAndProvider(providerUserId, UserIdentity.AuthProvider.GITHUB).orElseThrow(() -> new UsernameNotFoundException("找不到用户!"));
            User user = userRepository.findById(userIdentity.getUserId()).orElseThrow(() -> new BaseException(ResponseCodeConstants.USER_NOT_FOUND, "用户不存在", HttpStatus.UNAUTHORIZED));

            UserLoginInfo currentUser = objectMapper.convertValue(user, UserLoginInfo.class);//TODO 权限
            GitHubAuthentication token = new GitHubAuthentication(currentUser, true, List.of());
            // 构造认证对象
            return token;
        } catch (Exception e) {
            throw new BaseException(ResponseCodeConstants.ERROR, "GitHub OAuth2 登录失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return GitHubAuthentication.class.isAssignableFrom(authentication);
    }
}