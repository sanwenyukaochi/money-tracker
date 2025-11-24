package com.authentication.auth.authentication.handler.auth.github;

public record GitHubLoginRequest(
        String code
) {
}