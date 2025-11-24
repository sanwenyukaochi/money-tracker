package com.authentication.auth.common.web.service;

import com.authentication.auth.common.web.constant.ResponseCodeConstants;
import com.authentication.auth.common.web.exception.BaseException;
import lombok.RequiredArgsConstructor;
import com.authentication.auth.domain.model.entity.User;
import com.authentication.auth.domain.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserByPhone(String phoneNumber) {
        return userRepository.findByPhone(phoneNumber).orElseThrow(() -> new BaseException(ResponseCodeConstants.USER_PHONE_NOT_FOUND, "手机号不存在", HttpStatus.NOT_FOUND));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new BaseException(ResponseCodeConstants.USER_NOT_FOUND, "用户不存在", HttpStatus.NOT_FOUND));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new BaseException(ResponseCodeConstants.USER_EMAIL_NOT_FOUND, "邮箱不存在", HttpStatus.NOT_FOUND));
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new BaseException(ResponseCodeConstants.USER_NOT_FOUND, "用户不存在", HttpStatus.NOT_FOUND));
    }
}
