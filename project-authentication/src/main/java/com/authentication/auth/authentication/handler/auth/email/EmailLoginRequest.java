package com.authentication.auth.authentication.handler.auth.email;

public record EmailLoginRequest(
        String email,
        String password
) {}