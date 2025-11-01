package org.secure.security.authentication.handler.login.github.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.secure.security.authentication.handler.login.github.dto.GitHubAccessTokenRequest;
import org.secure.security.authentication.handler.login.github.dto.GitHubAccessTokenResponse;
import org.secure.security.authentication.handler.login.github.dto.GitHubUserProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GitHubOAuthService {

    @Value("${spring.security.oauth2.client.registration.github.client-id:}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.github.client-secret:}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    public GitHubAccessTokenResponse exchangeCodeForToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        GitHubAccessTokenRequest body = new GitHubAccessTokenRequest(clientId, clientSecret, code);
        HttpEntity<GitHubAccessTokenRequest> entity = new HttpEntity<>(body, headers);

        ResponseEntity<GitHubAccessTokenResponse> resp = restTemplate.exchange("https://github.com/login/oauth/access_token", HttpMethod.POST, entity, GitHubAccessTokenResponse.class);

        if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null && resp.getBody().accessToken() != null) {
            return resp.getBody();
        }
        throw new IllegalStateException("GitHub access_token 获取失败");
    }

    public GitHubUserProfile fetchUserProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<GitHubUserProfile> resp = restTemplate.exchange("https://api.github.com/user", HttpMethod.GET, entity, GitHubUserProfile.class);

        if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
            return resp.getBody();
        }
        throw new IllegalStateException("GitHub 用户信息获取失败");
    }

}
