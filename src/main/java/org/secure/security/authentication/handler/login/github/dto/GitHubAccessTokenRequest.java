package org.secure.security.authentication.handler.login.github.dto;

public record GitHubAccessTokenRequest(
        String clientId,
        String clientSecret,
        String code
) {}
