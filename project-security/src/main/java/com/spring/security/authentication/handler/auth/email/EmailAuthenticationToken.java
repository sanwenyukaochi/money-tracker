package com.spring.security.authentication.handler.auth.email;

import lombok.Getter;
import lombok.Setter;
import com.spring.security.authentication.handler.auth.UserLoginInfo;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Setter
@Getter
public class EmailAuthenticationToken extends AbstractAuthenticationToken {

    private String email;
    private String password;
    private UserLoginInfo currentUser;

    public EmailAuthenticationToken(String email, String password) {
        this.email = email;
        this.password = password;
        super(List.of());
        super.setAuthenticated(false);
    }

    public EmailAuthenticationToken(UserLoginInfo currentUser,
                                    Collection<? extends GrantedAuthority> authorities) {
        this.currentUser = currentUser;
        super(authorities);
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return isAuthenticated() ? null : password;
    }

    @Override
    public Object getPrincipal() {
        return isAuthenticated() ? currentUser : email;
    }
}