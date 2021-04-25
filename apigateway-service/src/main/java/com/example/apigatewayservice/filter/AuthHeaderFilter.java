package com.example.apigatewayservice.filter;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthHeaderFilter extends AbstractGatewayFilterFactory<AuthHeaderFilter.Config> {

    private final Environment env;

    //Gateway Filter 는 'filter' 메서드 하나만 가지고 있는 func interface.
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                return onError(exchange, "NO AUTHORIZATION HEADER", HttpStatus.UNAUTHORIZED);
            }
            String authHeaderValue = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String token = authHeaderValue.replace("Bearer ", "");
            if(!isJwtValid(token)){
                return onError(exchange, "JWT TOKEN IS NOT VALID", HttpStatus.UNAUTHORIZED);
            }


            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String errorMessage, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        log.info(errorMessage);
        return response.setComplete();
    }

    private boolean isJwtValid(String token) {
        String subject;
    }

    public static class Config {

    }
}
