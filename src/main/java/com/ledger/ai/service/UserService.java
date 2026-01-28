package com.ledger.ai.service;

import com.ledger.ai.enums.BaseCode;
import com.ledger.ai.exception.BaseException;
import lombok.RequiredArgsConstructor;
import com.ledger.ai.model.entity.User;
import com.ledger.ai.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new BaseException(BaseCode.USER_NOT_FOUND));
    }

    public User getUserByPhone(String phone) {
        return userRepository.findByPhone(phone).orElseThrow(() -> new BaseException(BaseCode.USER_PHONE_NOT_FOUND));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new BaseException(BaseCode.USER_EMAIL_NOT_FOUND));
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new BaseException(BaseCode.USER_NOT_FOUND));
    }
}
