package com.ledger.ai.authentication.handler.auth.message;

public record SmsLoginRequest(
        String phone,
        String captcha
) {
}