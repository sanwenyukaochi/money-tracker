package org.secure.security.authentication.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.secure.security.common.web.model.UserIdentity;
import org.secure.security.common.web.repository.UserIdentityRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserIdentityService {
    private final UserIdentityRepository userIdentityRepository;

    public UserIdentity getUserIdentityByProviderUserId(Long providerUserId, UserIdentity.AuthProvider provider) {
        return userIdentityRepository.findByProviderUserIdAndProvider(providerUserId, provider).orElseThrow(() -> new UsernameNotFoundException("找不到用户!"));
    }
}
