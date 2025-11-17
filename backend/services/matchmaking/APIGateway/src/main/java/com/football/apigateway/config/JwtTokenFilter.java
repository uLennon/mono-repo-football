package com.football.apigateway.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

@Component
public class JwtTokenFilter {
    @Value("${jwt.secret}")
    private String secret;

    public Mono<Boolean> validate(ServerHttpRequest request, ServerWebExchange exchange) {
        String path = request.getPath().toString();

        // Libera rotas de autenticação
        if (path.startsWith("/auth")) {
            return Mono.just(true);
        }

        String token = request.getHeaders().getFirst("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            System.out.println(request.getPath());
            System.out.println(path);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return Mono.just(false);
        }

        try {
            String jwt = token.substring(7);

            // Decodifica a chave Base64 — igual no auth-service
            byte[] keyBytes = Decoders.BASE64.decode(secret);
            SecretKey key = Keys.hmacShaKeyFor(keyBytes);

            // Valida o token
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt);

            return Mono.just(true);

        } catch (Exception e) {
            System.err.println("❌ JWT validation failed: " + e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return Mono.just(false);
        }
    }
}
