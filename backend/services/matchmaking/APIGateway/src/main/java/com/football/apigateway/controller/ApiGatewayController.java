package com.football.apigateway.controller;

import com.football.apigateway.config.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class ApiGatewayController {


    @Value("${services.auth}")
    private String authServiceUrl;

    @Value("${services.team}")
    private String teamServiceUrl;

    @Value("${services.match}")
    private String matchServiceUrl;

    @Value("${services.result}")
    private String resultServiceUrl;

    private final WebClient.Builder webClientBuilder;
    private final JwtTokenFilter jwtTokenFilter;

    public ApiGatewayController(WebClient.Builder webClientBuilder,JwtTokenFilter jwtTokenFilter) {
         this.webClientBuilder = webClientBuilder;
         this.jwtTokenFilter = jwtTokenFilter;
    }

    @RequestMapping("/auths/**")
    public Mono<ResponseEntity<String>> handleAuthRequest(ServerHttpRequest request,
                                                          @RequestBody(required = false) String body) {
        return forwardRequest(request, authServiceUrl, body);
    }

    @RequestMapping("/teams/**")
    public Mono<ResponseEntity<String>> handleTeamRequest(ServerHttpRequest request,
                                                          ServerWebExchange exchange,
                                                          @RequestBody(required = false) String body) {
        return jwtTokenFilter.validate(request, exchange)
                .flatMap(valid -> valid
                        ? forwardRequest(request, teamServiceUrl, body)
                        : Mono.just(ResponseEntity.status(401).body("Invalid or missing token")));
    }

    @RequestMapping("/matchs/**")
    public Mono<ResponseEntity<String>> handleMatchRequest(ServerHttpRequest request,
                                                           ServerWebExchange exchange,
                                                           @RequestBody(required = false) String body) {
        return jwtTokenFilter.validate(request, exchange)
                .flatMap(valid -> valid
                        ? forwardRequest(request, matchServiceUrl, body)
                        : Mono.just(ResponseEntity.status(401).body("Invalid or missing token")));
    }


    @RequestMapping("/results/**")
    public Mono<ResponseEntity<String>> handleResultRequest(ServerHttpRequest request,
                                                           ServerWebExchange exchange,
                                                           @RequestBody(required = false) String body) {
        String token = request.getQueryParams().getFirst("token");

        // cria uma nova referência
        var mutatedRequest = (token != null)
                ? request.mutate().header("Authorization", "Bearer " + token).build()
                : request;

        return jwtTokenFilter.validate(mutatedRequest, exchange)
                .flatMap(valid -> valid
                        ? forwardRequest(mutatedRequest, resultServiceUrl, body)
                        : Mono.just(ResponseEntity.status(401).body("Invalid or missing token")));
    }

    private Mono<ResponseEntity<String>> forwardRequest(ServerHttpRequest request, String serviceUrl, String body) {
        String path = request.getPath().pathWithinApplication().value().replaceFirst("^/api", "");


        String query = request.getURI().getQuery();

        if (query != null && !query.isEmpty()) {
            path = path + "?" + query;
            path = path.replaceAll("%0A", "").replaceAll("\n", "");
        }

        String targetUrl = serviceUrl + path;
        System.out.println(targetUrl);

        return webClientBuilder.build()
                .method(request.getMethod())
                .uri(targetUrl)
                .headers(headers -> headers.addAll(request.getHeaders()))
                .bodyValue(body == null ? "" : body)
                .retrieve()
                .toEntity(String.class);
    }
}
