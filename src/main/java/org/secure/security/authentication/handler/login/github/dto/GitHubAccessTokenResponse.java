package org.secure.security.authentication.handler.login.github.dto;

public record GitHubAccessTokenResponse(
        String accessToken,
        String tokenType,
        String scope
) {}
