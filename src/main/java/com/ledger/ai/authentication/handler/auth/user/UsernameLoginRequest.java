package com.ledger.ai.authentication.handler.auth.user;

public record UsernameLoginRequest(
        String username,
        String password
) {
}