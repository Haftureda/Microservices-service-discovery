package com.haftu.apigateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Log all incoming requests
        logger.info("Incoming request: {} {}", request.getMethod(), request.getPath());

        // Check for API key in header (simple authentication)
        String apiKey = request.getHeaders().getFirst("X-API-Key");

        if (request.getPath().value().startsWith("/api/") &&
                !isPublicEndpoint(request.getPath().value())) {

            if (apiKey == null || !isValidApiKey(apiKey)) {
                logger.warn("Unauthorized access attempt: {}", request.getPath());
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                DataBuffer buffer = response.bufferFactory()
                        .wrap("{\"error\": \"Unauthorized\", \"message\": \"Invalid or missing API key\"}".getBytes());
                return response.writeWith(Mono.just(buffer));
            }
        }

        return chain.filter(exchange);
    }

    private boolean isPublicEndpoint(String path) {
        return path.contains("/api/stocks/") &&
                (path.endsWith("/check") || path.contains("/api/stocks/123456"));
    }

    private boolean isValidApiKey(String apiKey) {
        // In production, validate against database or configuration
        return "test-api-key-2024".equals(apiKey);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}