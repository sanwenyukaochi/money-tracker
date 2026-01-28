package com.ledger.ai.authentication.handler.auth.jwt.dto;

public record JwtTokenUserLoginInfo(
        String sessionId,
        String username
) {
}
