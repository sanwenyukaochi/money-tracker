package org.secure.security.authentication.handler.resourceapi.openapi1;

import lombok.Getter;
import lombok.Setter;
import org.secure.security.authentication.handler.login.UserLoginInfo;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Setter
@Getter
public class MyJwtAuthentication extends AbstractAuthenticationToken {

    private String jwtToken; // 前端传过来
    private UserLoginInfo currentUser; // 认证成功后，后台从数据库获取信息
    public MyJwtAuthentication(String jwtToken, UserLoginInfo currentUser, Boolean authenticated,
                               Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.jwtToken = jwtToken;
        this.currentUser = currentUser;
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
