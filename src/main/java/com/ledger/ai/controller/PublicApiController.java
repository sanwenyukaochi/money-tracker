package com.ledger.ai.controller;

import com.ledger.ai.model.dto.Result;
import com.ledger.ai.model.entity.User;
import com.ledger.ai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/public-api")
@RequiredArgsConstructor
public class PublicApiController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @GetMapping
    public Result<Map<String, String>> encode() {
        String encode = passwordEncoder.encode("admin");
        return Result.success(Map.of(
                "passwordEncoder", Optional.ofNullable(encode).orElseThrow(IllegalArgumentException::new)
        ));
    }

    @GetMapping("/user")
    public Result<Page<@NonNull Map<String, Object>>> userPage(Pageable pageable) {
        Page<@NonNull User> userPage = userRepository.findAll(pageable);
        return Result.success(userPage.map(user -> Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "phone", user.getPhone()
        )));
    }

}
