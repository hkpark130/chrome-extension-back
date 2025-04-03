package kr.co.direa.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.*;

@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityGatewayConfig {
    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())) // Keycloak JWT 인증
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/", "/favicon.ico", "/workspace/bookmark/health", "/actuator/**",
                        "/logout/**", "/oauth2/**", "/login/**", "/external/ai",
                        "/workspace/actuator/**", "/dashboard/actuator/**", "/external/actuator/**"
                ).permitAll()
                .pathMatchers("/admin").hasAuthority("Admin")
                .anyExchange().authenticated() // 그 외 모든 요청은 인증 필요
            )
            .csrf(ServerHttpSecurity.CsrfSpec::disable) // REST API 사용 시 CSRF 필요 없음
            .cors(c->c.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return (authorities) -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

            authorities.forEach(authority -> {
                if (OidcUserAuthority.class.isInstance(authority)) {
                    OidcUserAuthority oidcUserAuthority = (OidcUserAuthority)authority;

//                    OidcIdToken idToken = oidcUserAuthority.getIdToken();
                    OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();

                    List<String> group = userInfo.getClaim("groups");
                    Optional.ofNullable(group).ifPresent(
                            groupList -> groupList.forEach(it -> {
//                            mappedAuthorities.add(
//                                new SimpleGrantedAuthority(it.replace("/", ""))
//                            );
                                        if (it.replace("/", "").equals("Admin")) {
                                            mappedAuthorities.add(new SimpleGrantedAuthority("Admin"));
                                            // 그룹 리스트 중에 Admin 이 포함되면 관리자로 매핑
                                        }
                                    }
                            ));
                }
            });

            return mappedAuthorities;
        };
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}