package com.authentication.auth.authentication.handler.auth.github.dto;

public record GitHubOAuthMeta(
        Boolean requiresBinding,
        Long githubId
) {
}
