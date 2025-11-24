package com.authentication.auth.authentication.handler.auth.message;

import lombok.Getter;
import lombok.Setter;
import com.authentication.auth.authentication.handler.auth.UserLoginInfo;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Setter
@Getter
public class SmsAuthenticationToken extends AbstractAuthenticationToken {

    private String phone;
    private String smsCode;
    private UserLoginInfo currentUser;

    public SmsAuthenticationToken(String phone, String smsCode, Boolean authenticated) {
        this.phone = phone;
        this.smsCode = smsCode;
        super(List.of());
        super.setAuthenticated(authenticated);
    }

    public SmsAuthenticationToken(UserLoginInfo currentUser, Boolean authenticated,
                                  Collection<? extends GrantedAuthority> authorities) {
        this.currentUser = currentUser;
        super(authorities);
        super.setAuthenticated(authenticated);
    }

    @Override
    public Object getCredentials() {
        return isAuthenticated() ? null : smsCode;
    }

    @Override
    public Object getPrincipal() {
        return isAuthenticated() ? currentUser : phone;
    }

}