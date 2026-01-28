package com.ledger.ai.controller;

import com.ledger.ai.authentication.handler.auth.UserLoginInfo;
import com.ledger.ai.model.dto.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.json.JsonMapper;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/info")
    public Result<UserLoginInfo> getUserInfo(Authentication authentication) {
        UserLoginInfo userLoginInfo = (UserLoginInfo) authentication.getPrincipal();
        log.info("用户登录信息：{}", JsonMapper.shared().writeValueAsString(userLoginInfo));
        return Result.success(userLoginInfo);
    }

}
