package org.secure.security.authentication.handler.login.github.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GitHubAccessTokenResponse(
        String accessToken,
        String tokenType,
        String scope
) {}
