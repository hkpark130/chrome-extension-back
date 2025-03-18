package kr.co.direa.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<Object> {

    // Keycloak realm URL (실제 서버와 realm에 맞게 수정)
    private static final String KEYCLOAK_REALM_URL = "https://<keycloak-server>/auth/realms/<realm>";


    public JwtAuthFilter() {
        super(Object.class);
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            // 헤더의 Authorization 값을 추출
            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

            // 토큰이 없거나 Bearer 형식이 아닐 경우 401 반환
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // JWT 검증 로직 (필요에 따라 실제 검증 로직으로 교체)
            if (!isValidToken(authHeader)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // 토큰이 유효하면 다음 필터 체인으로 전달
            return chain.filter(exchange);
        };
    }

    private boolean isValidToken(String token) {
        // "Bearer " 접두어 제거
        String actualToken = token.substring(7);
        try {
            PublicKey publicKey = getKeycloakPublicKey();
            // JWT 파싱 및 검증 (서명, 만료시간 등 검증)
            Jwts.parser().setSigningKey(publicKey).build().parseSignedClaims(actualToken);
            return true;
        } catch (Exception e) {
            // 예외 발생 시 검증 실패로 판단
            e.printStackTrace();
            return false;
        }
    }

    private PublicKey getKeycloakPublicKey() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        // Keycloak Realm 정보를 조회하여 JSON 응답 내 "public_key" 값을 획득
        Map<String, Object> realmInfo = restTemplate.getForObject(KEYCLOAK_REALM_URL, Map.class);
        if (realmInfo == null || !realmInfo.containsKey("public_key")) {
            throw new Exception("Keycloak 공개키를 가져올 수 없습니다.");
        }

        String publicKeyString = (String) realmInfo.get("public_key");
        // Keycloak이 제공하는 공개키는 일반적으로 PEM 헤더/푸터 없이 base64 인코딩된 문자열임
        byte[] decodedKey = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(keySpec);
    }
}
