package org.secure.security.authentication.handler.login.github;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.secure.security.authentication.handler.login.UserLoginInfo;
import org.secure.security.authentication.handler.login.github.dto.GitHubAccessTokenResponse;
import org.secure.security.authentication.handler.login.github.dto.GitHubUserProfile;
import org.secure.security.authentication.handler.login.github.service.GitHubOAuthService;
import org.secure.security.authentication.service.UserIdentityService;
import org.secure.security.authentication.service.UserService;
import org.secure.security.common.web.model.User;
import org.secure.security.common.web.model.UserIdentity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GitHubAuthenticationProvider implements AuthenticationProvider {

    private final GitHubOAuthService githubOAuthService;

    private final UserService userService;

    private final UserIdentityService userIdentityService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String code = (String) authentication.getPrincipal();
        try {
            GitHubAccessTokenResponse response = githubOAuthService.exchangeCodeForToken(code);
            GitHubUserProfile profile = githubOAuthService.fetchUserProfile(response.accessToken());

            UserIdentity userIdentity = userIdentityService.getUserIdentityByProviderUserId(profile.id(), UserIdentity.AuthProvider.GITHUB);
            User user = userService.findById(userIdentity.getUserId());

            ObjectMapper objectMapper = new ObjectMapper();
            UserLoginInfo currentUser = objectMapper.convertValue(user, UserLoginInfo.class);
            GitHubAuthentication token = new GitHubAuthentication(currentUser, true, List.of());
            // 构造认证对象
            return token;
        } catch (Exception e) {
            throw new BadCredentialsException("GitHub OAuth2 登录失败: " + e.getMessage());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return GitHubAuthentication.class.isAssignableFrom(authentication);
    }
}