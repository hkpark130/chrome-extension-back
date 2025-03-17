package kr.co.direa.gateway.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    public GlobalFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Global Filter baseMessage: {}, {}", config.getBaseMessage(), request.getRemoteAddress());
            // ✅ 요청이 들어올 때 로그 출력
            if (config.isPreLogger()) {
                log.info("🌍 [Global Filter] Request ID: {}, Path: {}", request.getId(), request.getPath());
            }

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                // ✅ 응답이 나갈 때 로그 출력
                if (config.isPostLogger()) {
                    log.info("🌍 [Global Filter] Response Code: {}", response.getStatusCode());
                }
            }));
        };
    }

    @Data
    public static class Config {
        private String baseMessage;
        private boolean preLogger;  // 요청 로깅 여부
        private boolean postLogger; // 응답 로깅 여부
    }
}
