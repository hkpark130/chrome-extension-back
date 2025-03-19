package kr.co.direa.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityGatewayConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // API 기반이므로 CSRF 비활성화
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/bookmarks/health").permitAll()  // healthcheck 요청은 인증 없이 허용
                        .anyExchange().authenticated()  // 🔥 모든 API 요청에 대해 인증 필요
                )
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt(Customizer.withDefaults())
                ); // 🔥 Keycloak JWT 검증 활성화 (issuer-uri는 application.yml에서 가져옴)

        return http.build();
    }
}