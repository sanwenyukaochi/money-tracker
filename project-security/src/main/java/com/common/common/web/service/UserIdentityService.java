package com.common.common.web.service;

import com.common.common.web.constant.ResponseCodeConstants;
import com.common.common.web.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.common.domain.model.entity.UserIdentity;
import com.common.domain.repository.UserIdentityRepository;
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
