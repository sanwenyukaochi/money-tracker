package com.secure.security.security.initializer;

import cn.hutool.core.lang.Snowflake;
import com.secure.security.model.entity.Role;
import com.secure.security.model.entity.User;
import com.secure.security.model.entity.relation.UserRole;
import com.secure.security.repository.RoleRepository;
import com.secure.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Initializer {
    private final Snowflake snowflake;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Bean
    public CommandLineRunner initData() {
        return args -> {
            Role userRole = roleRepository.findByCode("ROLE_USER")
                    .orElseGet(() -> {Role role = new Role();role.setId(snowflake.nextId());role.setName("用户");role.setCode("ROLE_USER");return roleRepository.save(role);});

            Role adminRole = roleRepository.findByCode("ROLE_ADMIN")
                    .orElseGet(() -> {Role role = new Role();role.setId(snowflake.nextId());role.setName("管理员");role.setCode("ROLE_ADMIN");return roleRepository.save(role);});

            if (!userRepository.existsByUsername("user")) {
                User user = new User();
                user.setId(snowflake.nextId());
                user.setUsername("user");
                user.setEmail("user@example.com");
                user.setPassword(passwordEncoder.encode("user"));
                user.setSignUpMethod("email");
                user.setTwoFactorEnabled(false);
                UserRole role = new UserRole();
                role.setId(snowflake.nextId());
                role.setUser(user);
                role.setRole(userRole);
                user.getRoles().add(role);
                userRepository.save(user);
            }

            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setId(snowflake.nextId());
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setSignUpMethod("email");
                admin.setTwoFactorEnabled(false);
                UserRole role = new UserRole();
                role.setId(snowflake.nextId());
                role.setUser(admin);
                role.setRole(adminRole);
                admin.getRoles().add(role);
                userRepository.save(admin);
            }
        };
    }
}
