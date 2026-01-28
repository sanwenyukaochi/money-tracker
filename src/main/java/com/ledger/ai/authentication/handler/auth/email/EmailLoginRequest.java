package com.ledger.ai.authentication.handler.auth.email;

public record EmailLoginRequest(
        String email,
        String password
) {}