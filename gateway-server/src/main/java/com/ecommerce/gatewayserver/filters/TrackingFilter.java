package com.ecommerce.gatewayserver.filters;

import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@Order(1)
public class TrackingFilter implements GlobalFilter {

    public static final String CORRELATION_ID = "tmx-correlation-id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        HttpHeaders headers = exchange.getRequest().getHeaders();
        String correlationId;

        if (headers.containsKey(CORRELATION_ID)) {
            correlationId = headers.getFirst(CORRELATION_ID);
        } else {
            correlationId = UUID.randomUUID().toString();
        }

        // Put it in MDC for logging
        MDC.put(CORRELATION_ID, correlationId);

        // Add it to request headers for downstream
        exchange = exchange.mutate()
                .request(exchange.getRequest().mutate()
                        .header(CORRELATION_ID, correlationId)
                        .build())
                .build();

        return chain.filter(exchange)
                .doFinally(signal -> MDC.remove(CORRELATION_ID)); // clean up MDC
    }
}