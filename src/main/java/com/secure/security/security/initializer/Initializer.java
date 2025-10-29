package com.secure.security.security.initializer;

import cn.hutool.core.lang.Snowflake;
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
        };
    }
}
