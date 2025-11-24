package com.authentication.auth.authentication.handler.auth.user;

public record UsernameLoginRequest(
        String username,
        String password
) {
}