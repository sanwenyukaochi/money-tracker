package com.authentication.auth.authentication.handler.auth;

import lombok.*;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * 用户信息登陆后的信息，会序列化到Jwt的payload
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginInfo implements UserDetails {

    private String sessionId; // 会话id，全局唯一
    private Long id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private Boolean accountNonLocked;
    private Boolean accountNonExpired;
    private Boolean credentialsNonExpired;
    private Boolean enabled;
    private String twoFactorSecret;
    private Boolean twoFactorEnabled;
    private Long expiredTime; // JWT过期时间

    @Override
    public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
        //TODO 权限
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
