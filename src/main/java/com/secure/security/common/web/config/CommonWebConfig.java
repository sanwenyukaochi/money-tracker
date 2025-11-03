package com.secure.security.common.web.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Optional;

@Configuration
@EnableConfigurationProperties(CommonWebConfig.ProxyProperties.class)
public class CommonWebConfig {

    @Bean
    public Snowflake snowflake() {
        return IdUtil.getSnowflake(1, 1);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonWriteFeature.WRITE_NUMBERS_AS_STRINGS.mappedFeature(), true);
        return objectMapper;
    }

    @Bean
    public RestTemplate restTemplate(ProxyProperties proxyProperties) {

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(8000);

        Optional.of(proxyProperties)
                .filter(ProxyProperties::enabled)
                .ifPresent(p -> factory.setProxy(
                        new Proxy(Proxy.Type.HTTP, new InetSocketAddress(p.host(), p.port()))
                ));

        return new RestTemplate(factory);
    }

    @ConfigurationProperties(prefix = "spring.proxy")
    public record ProxyProperties(boolean enabled, String host, int port) {
    }

}
