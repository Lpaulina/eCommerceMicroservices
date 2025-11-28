package com.ecommerce.gatewayserver.filters;

import brave.Span;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import brave.Tracer;
import reactor.core.publisher.Mono;

@Configuration
public class ResponseFilter {

    final Logger logger =LoggerFactory.getLogger(ResponseFilter.class);

    @Autowired
    Tracer tracer;

    @Autowired
    FilterUtils filterUtils;

    @Bean
    public GlobalFilter postGlobalFilter(Tracer tracer) {
        return (exchange, chain) -> chain.filter(exchange)
                .then(Mono.deferContextual(ctx -> {
                    // Brave stores the current span in the reactive context
                    Span currentSpan = tracer.currentSpan();

                    String traceId = (currentSpan != null) ? String.valueOf(currentSpan.context().traceId()) : "N/A";

                    exchange.getResponse().getHeaders().add("X-Trace-Id", traceId);
                    logger.debug("Added traceId to response headers: {}", traceId);

                    return Mono.empty();
                }));
    }

}