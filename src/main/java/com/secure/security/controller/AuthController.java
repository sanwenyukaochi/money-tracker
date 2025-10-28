package com.secure.security.controller;

import com.secure.security.model.dto.Result;
import com.secure.security.security.request.LoginRequest;
import com.secure.security.security.request.SignupRequest;
import com.secure.security.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/public/signIn")
    public Result<?> signIn(@Valid @RequestBody LoginRequest loginRequest) {
        String jwtToken = authService.signIn(loginRequest);
        return Result.success(Map.of("jwtToken", jwtToken));
    }

    @PostMapping("/public/signUp")
    public Result<?> signUp(@Valid @RequestBody SignupRequest signUpRequest) {
        authService.signUp(signUpRequest);
        return Result.success();
    }

}