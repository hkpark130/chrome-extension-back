package kr.co.direa.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.web.server.ServerAuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
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
    @Value("${constants.frontend}") private String frontend;

    @Bean
    public ServerAuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                     ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
        http
            .oauth2Login(Customizer.withDefaults()) // Keycloak OAuth2 로그인
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())) // Keycloak JWT 인증
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/", "/favicon.ico", "/bookmarks/health",
                        "/logout/**", "/oauth2/**", "/login/**").permitAll()
                .pathMatchers("/admin").hasAuthority("Admin")
                .anyExchange().authenticated() // 그 외 모든 요청은 인증 필요
            )
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint((exchange, ex) -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                })
            )
            .csrf(ServerHttpSecurity.CsrfSpec::disable) // REST API 사용 시 CSRF 필요 없음
            .cors(c->c.configurationSource(corsConfigurationSource()));

        http.oauth2Login(oauth2 -> oauth2
                .authorizationRequestRepository(authorizationRequestRepository())
            .authenticationSuccessHandler(new JwtAuthenticationSuccessHandler(authorizedClientRepository, frontend)) // ✅ 성공 핸들러 적용
            .authenticationFailureHandler((webFilterExchange, exception) -> {
                log.error("🚨 로그인 실패! 에러: {}", exception.getMessage());
                webFilterExchange.getExchange().getResponse().setStatusCode(HttpStatus.FOUND);
                webFilterExchange.getExchange().getResponse().getHeaders().setLocation(java.net.URI.create(frontend + "/login-failed"));
                return webFilterExchange.getExchange().getResponse().setComplete();
            })
        );

//        http.logout((logout) -> {
//            var logoutSuccessHandler =
//                    new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
//            logoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}/");
//            logout.logoutSuccessHandler(logoutSuccessHandler);
//        });
//        .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
//        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER));

//        http.logout(logout -> logout
//                .deleteCookies("JSESSIONID", "loggedIn")
//                .clearAuthentication(true)
//                .invalidateHttpSession(true)
//                .logoutSuccessHandler(logoutSuccessHandler()));

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
        configuration.setAllowedOrigins(List.of(frontend));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}