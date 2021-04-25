package com.example.apigatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Ordered Gateway Filter 를 만드는 코드
 * HIGH PRECEDENCE 의 경우 Global Filter 보다 먼저 적용된다.
 */

@Slf4j
@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        //Custom Pre Filter
//        return (exchange, chain) -> {
//            ServerHttpRequest request = exchange.getRequest();
//            ServerHttpResponse response = exchange.getResponse();
//
//            log.info("Global Filter baseMessage : {}", config.getBaseMessage());
//
//            if(config.isPreLogger()){
//                log.info("Global Start requestId : {}", request.getId());
//            }
//
//            return chain.filter(exchange).then(Mono.fromRunnable(()->{
//                if(config.isPostLogger()){
//                    log.info("Global End response code : {}", response.getStatusCode());
//                }
//            }));
//        };

        OrderedGatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Logging Filter baseMessage : {}", config.getBaseMessage());

            if(config.isPreLogger()){
                log.info("Logging PRE requestId : {}", request.getId());
            }

            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if(config.isPostLogger()){
                    log.info("Logging POST response code : {}", response.getStatusCode());
                }
            }));

        }, Ordered.HIGHEST_PRECEDENCE);


        return filter;
    }
    @Data
    public static class Config{
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }

}
