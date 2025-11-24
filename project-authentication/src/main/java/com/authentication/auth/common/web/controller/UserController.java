package com.authentication.auth.common.web.controller;

import com.authentication.auth.authentication.handler.auth.UserLoginInfo;
import com.authentication.auth.domain.model.dto.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/info")
    public Result<UserLoginInfo> getUserInfo(Authentication authentication) {
        UserLoginInfo userLoginInfo = (UserLoginInfo) authentication.getPrincipal();
        log.info("自家用户登录信息：{}", userLoginInfo);
        return Result.success("用户信息获取成功", userLoginInfo);
    }

}
