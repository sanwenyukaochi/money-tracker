package org.secure.security.authentication.handler.login.github.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.RequiredArgsConstructor;
import org.secure.security.authentication.handler.login.github.dto.GitHubAccessTokenRequest;
import org.secure.security.authentication.handler.login.github.dto.GitHubAccessTokenResponse;
import org.secure.security.authentication.handler.login.github.dto.GitHubUserProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GitHubOAuthService {

    @Value("${spring.security.oauth2.client.registration.github.client-id:}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.github.client-secret:}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public GitHubAccessTokenResponse exchangeCodeForToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        GitHubAccessTokenRequest body = new GitHubAccessTokenRequest(clientId, clientSecret, code);
        HttpEntity<GitHubAccessTokenRequest> entity = new HttpEntity<>(body, headers);

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        factory.setProxy(proxy);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(mapper);

        RestTemplate proxyRestTemplate = new RestTemplate(factory);
        proxyRestTemplate.getMessageConverters().removeIf(c -> c instanceof MappingJackson2HttpMessageConverter);
        proxyRestTemplate.getMessageConverters().add(converter);

        ResponseEntity<GitHubAccessTokenResponse> resp = proxyRestTemplate.exchange("https://github.com/login/oauth/access_token", HttpMethod.POST, entity, GitHubAccessTokenResponse.class);

        if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null && resp.getBody().accessToken() != null) {
            return resp.getBody();
        }
        throw new IllegalStateException("GitHub access_token 获取失败");
    }

    public GitHubUserProfile fetchUserProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<HttpHeaders> entity = new HttpEntity<>(headers);

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        factory.setProxy(proxy);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(mapper);

        RestTemplate proxyRestTemplate = new RestTemplate(factory);
        proxyRestTemplate.getMessageConverters().removeIf(c -> c instanceof MappingJackson2HttpMessageConverter);
        proxyRestTemplate.getMessageConverters().add(converter);

        ResponseEntity<GitHubUserProfile> resp = proxyRestTemplate.exchange("https://api.github.com/user", HttpMethod.GET, entity, GitHubUserProfile.class);
        if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
            return resp.getBody();
        }
        throw new IllegalStateException("GitHub 用户信息获取失败");
    }






}