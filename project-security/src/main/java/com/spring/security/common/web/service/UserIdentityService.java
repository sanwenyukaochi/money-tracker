package com.spring.security.common.web.service;

import com.spring.security.common.web.constant.ResponseCodeConstants;
import com.spring.security.common.web.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.spring.security.domain.model.entity.UserIdentity;
import com.spring.security.domain.repository.UserIdentityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserIdentityService {

    private final UserIdentityRepository userIdentityRepository;

    public UserIdentity getUserIdentityByProviderUserIdAndProvider(Long providerUserId, UserIdentity.AuthProvider provider) {
        return userIdentityRepository.findByProviderUserIdAndProvider(providerUserId, provider).orElseThrow(() -> new BaseException(ResponseCodeConstants.USER_NOT_FOUND, "用户不存在", HttpStatus.NOT_FOUND));
    }
}
