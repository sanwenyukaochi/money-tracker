package com.authentication.auth.authentication.handler.auth.github.dto;

public record GitHubOAuthConfigResponse(
        String clientId,
        String redirectUri
) {
}
