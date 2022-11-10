package com.like.gateway.filter;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 安全拦截全局过滤器，非网关鉴权的逻辑
 * <p>
 * 在ResourceServerManager#check鉴权善后一些无关紧要的事宜(线上请求拦截、黑名单拦截)
 *
 * @author haoxr
 * @date 2022/2/15
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class GatewaySecurityFilter implements GlobalFilter, Ordered {

    @Value("${spring.profiles.active}")
    private String env;

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        exchange = exchange.mutate().request(request).build();
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
