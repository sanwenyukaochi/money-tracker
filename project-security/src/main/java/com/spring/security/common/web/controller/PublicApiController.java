package com.spring.security.common.web.controller;

import com.spring.security.domain.model.dto.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping
    public Result<Map<String, String>> encode() {
        String encode = passwordEncoder.encode("admin");
        return Result.success("执行成功", Map.of(
                "passwordEncoder", Optional.ofNullable(encode).orElseThrow(IllegalArgumentException::new)
        ));
    }
}
