package com.spring.gateway.config;

import com.spring.gateway.filter.PostGatewayFilterFactory;
import com.spring.gateway.filter.PreGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("authentication_route", r -> r
                        .path(
                                "/api/**",
                                "/api/public-api/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**"
                        )
                        .filters(f -> f
                                .filter(new PreGatewayFilterFactory().apply(new PreGatewayFilterFactory.Config()))
                                .filter(new PostGatewayFilterFactory().apply(new PostGatewayFilterFactory.Config()))
                        )
                        .uri("http://localhost:8081")
                )
                .build();
    }
}
