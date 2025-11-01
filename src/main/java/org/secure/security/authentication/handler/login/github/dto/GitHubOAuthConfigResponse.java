package org.secure.security.authentication.handler.login.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubOAuthConfigResponse(
        @JsonProperty("clientId")
        String clientId,
        @JsonProperty("redirectUri")
        String redirectUri
) {
}
