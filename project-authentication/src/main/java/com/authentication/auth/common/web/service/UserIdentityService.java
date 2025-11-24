package com.authentication.auth.common.web.service;

import com.authentication.auth.common.web.constant.ResponseCodeConstants;
import com.authentication.auth.common.web.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.authentication.auth.domain.model.entity.UserIdentity;
import com.authentication.auth.domain.repository.UserIdentityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserIdentityService {
    private final UserIdentityRepository userIdentityRepository;

    public UserIdentity getUserIdentityByProviderUserId(Long providerUserId, UserIdentity.AuthProvider provider) {
        return userIdentityRepository.findOptionalByProviderUserIdAndProvider(providerUserId, provider).orElseThrow(() -> new BaseException(ResponseCodeConstants.USER_NOT_FOUND, "用户不存在", HttpStatus.NOT_FOUND));
    }
}
