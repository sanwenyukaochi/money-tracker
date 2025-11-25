package com.spring.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class PostGatewayFilterFactory
        extends AbstractGatewayFilterFactory<PostGatewayFilterFactory.@NonNull Config> {

    public PostGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    @NonNull
    public GatewayFilter apply(Config config) {
        // grab configuration from Config object
        return (exchange, chain) -> {
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                ServerHttpResponse serverHttpResponse = exchange.getResponse();
                log.info("[PostFilter] 响应状态码: {}", serverHttpResponse.getStatusCode());
                //Manipulate the response in some way
            }));
        };
    }

    public static class Config {
        //Put the configuration properties for your filter here
    }
}
