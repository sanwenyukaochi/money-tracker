package com.secure.security.security.service;

import com.secure.security.model.entity.Permission;
import com.secure.security.model.entity.Role;
import com.secure.security.model.entity.User;
import com.secure.security.model.entity.relation.RolePermission;
import com.secure.security.model.entity.relation.UserRole;
import com.secure.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));
        List<Role> roleList = user.getRoles().stream().map(UserRole::getRole).toList();
        List<Permission> permissions = roleList.stream().flatMap(role -> role.getPermissions().stream()).map(RolePermission::getPermission).toList();
        return UserDetailsImpl.build(user, roleList.stream().map(Role::getCode).toList(), permissions.stream().map(Permission::getCode).toList());
    }
}
