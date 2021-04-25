package com.example.apigatewayservice.filter;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
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

import javax.crypto.SecretKey;
import java.security.Key;

/**
 * JWT 인증이 필요한 라우팅에서
 * JWT 인증을 담당하는 필터, 빈으로 등록되어야한다.
 */
@Slf4j
@Component
public class AuthHeaderFilter extends AbstractGatewayFilterFactory<AuthHeaderFilter.Config>
                                implements InitializingBean {

    private final String secret;
    private Key key;

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public AuthHeaderFilter(Environment env,
                            @Value("${token.secret}") String secret) {
        super(AuthHeaderFilter.Config.class);
        this.secret = secret;
    }

    //Gateway Filter 는 'filter' 메서드 하나만 가지고 있는 func interface.
    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            /**
             * Authorization 헤더가 없는 경우 실패
             */
            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                return onError(exchange, "NO AUTHORIZATION HEADER", HttpStatus.UNAUTHORIZED);
            }

            /**
             * 헤더에서 token 파싱
             */
            String authHeaderValue = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String token = authHeaderValue.replace("Bearer ", "");

            /**
             *  token 검증
             */
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

        String subject = null;
        try {
            subject = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return false;
        }

        if(subject == null || subject.isEmpty()){
            return false;
        }

        return true;
    }



    public static class Config {

    }
}
