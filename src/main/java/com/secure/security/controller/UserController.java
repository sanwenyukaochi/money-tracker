package com.secure.security.controller;

import com.secure.security.model.dto.Result;
import com.secure.security.security.service.UserPrincipal;
import com.secure.security.service.UserService;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAuthority('user:get')")
    @GetMapping("/info")
    public Result<?> getUserInfo(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return Result.success(Map.of("userInfo", userPrincipal));
    }

    @PreAuthorize("hasAuthority('user:post')")
    @PostMapping("/info")
    public Result<?> postUserInfo() {
        return Result.success(Map.of("message", "创建用户成功"));
    }

    @PreAuthorize("hasAuthority('user:put')")
    @PutMapping("/info")
    public Result<?> putUserInfo() {
        return Result.success(Map.of("message", "更新用户成功"));
    }

    @PreAuthorize("hasAuthority('user:delete')")
    @DeleteMapping("/info")
    public Result<?> deleteUserInfo() {
        return Result.success(Map.of("message", "删除用户成功"));
    }
}
