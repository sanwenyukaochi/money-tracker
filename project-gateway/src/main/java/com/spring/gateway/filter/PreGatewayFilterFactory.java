package com.spring.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

@Slf4j
@Component
public class PreGatewayFilterFactory
        extends AbstractGatewayFilterFactory<PreGatewayFilterFactory.@NonNull Config> {

    public PreGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    @NonNull
    public GatewayFilter apply(Config config) {
        // grab configuration from Config object
        return (exchange, chain) -> {
            //If you want to build a "pre" filter you need to manipulate the
            //request before calling chain.filter
            ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate().build();
            log.info("[PreFilter ] 请求进入网关: {}", exchange.getRequest().getURI());
            //use builder to manipulate the request
            return chain.filter(exchange.mutate().request(serverHttpRequest).build());
        };
    }

    public static class Config {
        //Put the configuration properties for your filter here
    }
}
