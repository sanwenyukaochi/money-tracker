package com.secure.security.service;

import com.secure.security.security.request.LoginRequest;
import com.secure.security.security.request.SignupRequest;

public interface AuthService {
    String signIn(LoginRequest loginRequest);

    void signUp(SignupRequest signUpRequest);
}
