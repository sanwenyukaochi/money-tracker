package com.spring.security.authentication.handler.auth.message;

import lombok.Getter;
import lombok.Setter;
import com.spring.security.authentication.handler.auth.UserLoginInfo;
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

    public SmsAuthenticationToken(String phone, String smsCode) {
        this.phone = phone;
        this.smsCode = smsCode;
        super(List.of());
        super.setAuthenticated(false);
    }

    public SmsAuthenticationToken(UserLoginInfo currentUser,
                                  Collection<? extends GrantedAuthority> authorities) {
        this.currentUser = currentUser;
        super(authorities);
        super.setAuthenticated(true);
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