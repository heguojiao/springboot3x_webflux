package com.example.webflux.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * 创建一个自定义过滤器工厂
 */
@Component
public class CustomGatewayFilterFactory extends AbstractGatewayFilterFactory<CustomGatewayFilterFactory.Config> {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomGatewayFilterFactory.class);

    public CustomGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            
            logger.info("Custom Gateway Filter Factory - Request path: {}", request.getPath());
            
            if (config.isPreLogger()) {
                logger.info("Custom Pre Filter: request path -> {}", request.getPath());
            }
            
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if (config.isPostLogger()) {
                    logger.info("Custom Post Filter: response status -> {}", 
                        exchange.getResponse().getStatusCode());
                }
            }));
        };
    }

    public static class Config {
        private boolean preLogger;
        private boolean postLogger;

        public boolean isPreLogger() {
            return preLogger;
        }

        public void setPreLogger(boolean preLogger) {
            this.preLogger = preLogger;
        }

        public boolean isPostLogger() {
            return postLogger;
        }

        public void setPostLogger(boolean postLogger) {
            this.postLogger = postLogger;
        }
    }
} 