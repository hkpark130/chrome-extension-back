package kr.co.direa.gateway.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.net.URI;
import java.time.Duration;

@Slf4j
public class JwtAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    private final ServerOAuth2AuthorizedClientRepository authorizedClientRepository;
    private final String frontendUrl; // React URL

    public JwtAuthenticationSuccessHandler(ServerOAuth2AuthorizedClientRepository authorizedClientRepository,
                                           @Value("${constants.frontend}") String frontendUrl) {
        this.authorizedClientRepository = authorizedClientRepository;
        this.frontendUrl = frontendUrl;
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpResponse response = exchange.getResponse();

        return authorizedClientRepository.loadAuthorizedClient("keycloak", authentication, exchange)
                .map(OAuth2AuthorizedClient::getAccessToken)
                .map(accessToken -> {

                    // ✅ JWT를 HttpOnly Secure 쿠키로 저장
                    ResponseCookie jwtCookie = ResponseCookie.from("jwt", accessToken.getTokenValue())
                            .httpOnly(true) // JavaScript에서 접근 방지 (XSS 보호)
//                          .secure(true)   // HTTPS에서만 전송 (배포 시 필수)
                            .path("/") // 모든 API 요청 시 포함
                            .sameSite("None") // Cross-Origin 지원 (필수)
                            .maxAge(Duration.ofHours(1)) // 쿠키 만료 시간 (1시간)
                            .build();

                    response.addCookie(jwtCookie);

                    // ✅ 로그인 성공 후 프론트엔드로 리디렉트
                    response.setStatusCode(HttpStatus.FOUND);
                    response.getHeaders().setLocation(URI.create(frontendUrl));

                    return response.setComplete();
                }).flatMap(Mono::from);
    }

}
