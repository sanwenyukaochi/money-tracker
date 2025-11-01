package org.secure.security.authentication.handler.login.github.dto;

public record GitHubOAuthConfigResponse(
        String clientId,
        String redirectUri
) {
}
