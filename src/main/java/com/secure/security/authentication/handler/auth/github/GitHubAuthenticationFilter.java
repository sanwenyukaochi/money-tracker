package com.secure.security.authentication.handler.auth.github;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Slf4j
public class GitHubAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final RequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = PathPatternRequestMatcher.withDefaults()
            .matcher(HttpMethod.POST, "/api/login/oauth/github");

    private final ObjectMapper objectMapper = new ObjectMapper();

    public GitHubAuthenticationFilter(AuthenticationManager authenticationManager,
                                      AuthenticationSuccessHandler authenticationSuccessHandler,
                                      AuthenticationFailureHandler authenticationFailureHandler) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        setAuthenticationSuccessHandler(authenticationSuccessHandler);
        setAuthenticationFailureHandler(authenticationFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.debug("use GithubAuthenticationFilter");

        GitHubLoginRequest githubLoginRequest = objectMapper.readValue(request.getInputStream(), GitHubLoginRequest.class);

        GitHubAuthenticationToken authentication = new GitHubAuthenticationToken(githubLoginRequest.code(), false);
        return getAuthenticationManager().authenticate(authentication);
    }
}