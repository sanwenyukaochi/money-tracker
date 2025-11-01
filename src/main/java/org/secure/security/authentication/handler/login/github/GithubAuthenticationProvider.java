package org.secure.security.authentication.handler.login.github;

import lombok.RequiredArgsConstructor;
import org.secure.security.authentication.handler.login.UserLoginInfo;
import org.secure.security.authentication.handler.login.github.dto.GithubUserProfile;
import org.secure.security.authentication.handler.login.github.service.GithubOAuthService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GithubAuthenticationProvider implements AuthenticationProvider {

    private final GithubOAuthService githubOAuthService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String code = (String) authentication.getPrincipal();
        try {
            String accessToken = githubOAuthService.exchangeCodeForToken(code);
            GithubUserProfile profile = githubOAuthService.fetchUserProfile(accessToken);

            UserLoginInfo currentUser = UserLoginInfo.builder()
                    .id(profile.getId())
                    .username(profile.getLogin())
                    .email(profile.getEmail())
                    .enabled(true)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .build();
            return new GithubAuthentication(currentUser, true, List.of());
        } catch (Exception e) {
            throw new BadCredentialsException("GitHub OAuth2 登录失败: " + e.getMessage());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return GithubAuthentication.class.isAssignableFrom(authentication);
    }
}