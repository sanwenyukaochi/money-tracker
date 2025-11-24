package com.authentication.auth.authentication.handler.auth.github;

import lombok.Getter;
import lombok.Setter;
import com.authentication.auth.authentication.handler.auth.UserLoginInfo;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Setter
@Getter
public class GitHubAuthenticationToken extends AbstractAuthenticationToken {

    private String code;
    private UserLoginInfo currentUser;

    public GitHubAuthenticationToken(String code, Boolean authenticated) {
        this.code = code;
        super(List.of());
        super.setAuthenticated(authenticated);
    }

    public GitHubAuthenticationToken(UserLoginInfo currentUser, Boolean authenticated,
                                     Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.currentUser = currentUser;
        super.setAuthenticated(authenticated);
    }

    @Override
    public Object getCredentials() {
        return isAuthenticated() ? null : code;
    }

    @Override
    public Object getPrincipal() {
        return isAuthenticated() ? currentUser : code;
    }
}
