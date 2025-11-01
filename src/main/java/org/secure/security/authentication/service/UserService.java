package org.secure.security.authentication.service;

import lombok.RequiredArgsConstructor;
import org.secure.security.common.web.model.User;
import org.secure.security.common.web.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserByPhone(String phoneNumber) {
        return userRepository.findByPhone(phoneNumber).orElseThrow(() -> new UsernameNotFoundException("手机号不存在"));
    }

    public User getUserFromDB(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
    }
}
