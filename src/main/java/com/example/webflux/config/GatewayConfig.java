package com.example.webflux.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
//import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

/**
 * 创建一个配置类来自定义网关的一些行为
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // 示例路由1：带有断言和过滤器的路由
//                .route("custom_route1", r -> r.path("/api/**")
//                        .filters(f -> f
//                                .addRequestHeader("X-Request-Custom", "CustomValue")
//                                .addResponseHeader("X-Response-Custom", "CustomValue")
//                                .rewritePath("/api/(?<segment>.*)", "/${segment}"))
//                        .uri("http://localhost:8081"))
                
                // 示例路由2：带有熔断和重试的路由
                .route("custom_route2", r -> r.path("/service/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("myCircuitBreaker")
                                        .setFallbackUri("forward:/fallback"))
                                .retry(config -> config
                                        .setRetries(3)
                                        .setStatuses(HttpStatus.BAD_GATEWAY)))
                        .uri("http://localhost:8082"))
                
                // 示例路由3：带有限流的路由
//                .route("custom_route3", r -> r.path("/limited/**")
//                        .filters(f -> f
//                                .requestRateLimiter(config -> config
//                                        .setRateLimiter(redisRateLimiter())
//                                        .setKeyResolver(userKeyResolver())))
//                        .uri("http://localhost:8083"))
                .build();
    }

//    @Bean
//    public RedisRateLimiter redisRateLimiter() {
//        return new RedisRateLimiter(10, 20, 1);
//    }

    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just(
            exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
        );
    }
} 