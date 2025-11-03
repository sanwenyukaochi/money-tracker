package com.secure.security.authentication.handler.auth.jwt;

import lombok.Getter;
import lombok.Setter;
import com.secure.security.authentication.handler.auth.UserLoginInfo;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Setter
@Getter
public class JwtTokenAuthentication extends AbstractAuthenticationToken {

    private String jwtToken; // 前端传过来
    private UserLoginInfo currentUser; // 认证成功后，后台从数据库获取信息

    public JwtTokenAuthentication(String jwtToken, Boolean authenticated) {
        this.jwtToken = jwtToken;
        super(null); // 权限，用不上，直接null
        super.setAuthenticated(authenticated);
    }

    public JwtTokenAuthentication(UserLoginInfo currentUser, Boolean authenticated,
                                  Collection<? extends GrantedAuthority> authorities) {
        this.currentUser = currentUser;
        super(authorities);
        super.setAuthenticated(authenticated);
    }

    @Override
    public Object getCredentials() {
        // 根据SpringSecurity的设计，授权成后，Credential（比如，登录密码）信息需要被清空
        return isAuthenticated() ? null : jwtToken;
    }

    @Override
    public Object getPrincipal() {
        // 根据SpringSecurity的设计，授权成功之前，getPrincipal返回的客户端传过来的数据。授权成功后，返回当前登陆用户的信息
        return isAuthenticated() ? currentUser : jwtToken;
    }

}
