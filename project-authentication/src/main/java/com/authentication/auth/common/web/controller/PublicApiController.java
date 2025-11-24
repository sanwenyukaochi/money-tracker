package com.authentication.auth.common.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/public-api")
@RequiredArgsConstructor
public class PublicApiController {
    private final PasswordEncoder passwordEncoder;
    @GetMapping
    public void test() {
        String encode = passwordEncoder.encode("admin");
        System.out.println(encode);
    }
}
