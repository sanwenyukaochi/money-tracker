package com.secure.security.security.service;

import com.secure.security.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private Long id;
    private String username;
    private String email;
    private String password;
    private Boolean accountNonLocked;
    private Boolean accountNonExpired;
    private Boolean credentialsNonExpired;
    private Boolean enabled;

    private String twoFactorSecret;
    private boolean isTwoFactorEnabled;
    private String signUpMethod;

    private List<String> roles;
    private List<String> permissions;

    public static UserDetails build(User user, List<String> roles, List<String> permissions) {
        return UserDetailsImpl.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .accountNonLocked(user.getAccountNonLocked())
                .accountNonExpired(user.getAccountNonExpired())
                .credentialsNonExpired(user.getCredentialsNonExpired())
                .enabled(user.getEnabled())
                .twoFactorSecret(user.getTwoFactorSecret())
                .isTwoFactorEnabled(user.isTwoFactorEnabled())
                .signUpMethod(user.getSignUpMethod())
                .roles(roles)
                .permissions(permissions)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (roles != null) {
            authorities.addAll(roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList());
        }
        if (permissions != null) {
            authorities.addAll(permissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList());
        }
        return authorities;
    }
}