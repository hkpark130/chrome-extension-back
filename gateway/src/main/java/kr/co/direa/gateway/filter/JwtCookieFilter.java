package kr.co.direa.gateway.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class JwtCookieFilter extends AbstractGatewayFilterFactory<JwtCookieFilter.Config> {
    private final ServerOAuth2AuthorizedClientRepository authorizedClientRepository;

    @Data
    public static class Config {
    }

    public JwtCookieFilter(ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
        super(Config.class);
        this.authorizedClientRepository = authorizedClientRepository;
    }

    @Override
    public GatewayFilter apply(Config config) {
//        GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
//            ServerHttpRequest request = exchange.getRequest();
//            ServerHttpResponse response = exchange.getResponse();
//
//            log.info("Logging Filter baseMessage: {}", config.getBaseMessage());
//            if (config.isPreLogger()) {
//                log.info("Logging PRE Filter: request id -> {}", request.getId());
//            }
//            return chain.filter(exchange).then(Mono.fromRunnable(()->{
//                if (config.isPostLogger()) {
//                    log.info("Logging POST Filter: response code -> {}", response.getStatusCode());
//                }
//            }));
//        }, Ordered.HIGHEST_PRECEDENCE);

//        return filter;
        return null;
    }

//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        return exchange.getPrincipal()
//                .filter(principal -> principal instanceof OAuth2AuthenticatedPrincipal)
//                .cast(OAuth2AuthenticatedPrincipal.class)
//                .flatMap(principal -> authorizedClientRepository
//                        .loadAuthorizedClient("keycloak", principal, exchange)
//                        .map(client -> client.getAccessToken().getTokenValue()))
//                .doOnNext(accessToken -> {
//                    ResponseCookie cookie = ResponseCookie.from("jwt", accessToken)
//                            .httpOnly(true)
//                            .secure(true)
//                            .path("/")
//                            .sameSite("None")
//                            .build();
//                    exchange.getResponse().addCookie(cookie);
//                })
//                .then(chain.filter(exchange));
//    }
}
