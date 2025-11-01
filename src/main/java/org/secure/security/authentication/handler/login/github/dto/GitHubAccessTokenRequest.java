package org.secure.security.authentication.handler.login.github.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GitHubAccessTokenRequest(
        String clientId,
        String clientSecret,
        String code
) {}
