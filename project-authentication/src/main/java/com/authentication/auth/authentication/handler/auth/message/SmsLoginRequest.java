package com.authentication.auth.authentication.handler.auth.message;

public record SmsLoginRequest(
        String phone,
        String captcha
) {
}