package kr.co.direa.workspace.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakService {
    private final WebClient.Builder webClientBuilder;
    @Value("${keycloak.base-url}")
    private String keycloakBaseUrl;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.admin-client.client-id}")
    private String clientId;
    @Value("${keycloak.admin-client.client-secret}")
    private String clientSecret;
    /**
     * 사용자 UUID 기준으로 Keycloak에서 이름(fullName) 조회
     */
    public String getFullNameByUserId(UUID userId) {
        try {
            String accessToken = getAdminAccessToken();
            Map<String, Object> user = webClientBuilder.build()
                    .get()
                    .uri(keycloakBaseUrl + "/admin/realms/" + realm + "/users/" + userId)
                    .headers(headers -> headers.setBearerAuth(accessToken))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            String firstName = (String) user.getOrDefault("firstName", "");
            String lastName = (String) user.getOrDefault("lastName", "");
            return (firstName + " " + lastName).trim();
        } catch (Exception e) {
            log.error("❌ Keycloak 사용자 정보 조회 실패: {}", e.getMessage());
            return "Unknown";
        }
    }
    /**
     * 관리용 클라이언트(client_credentials)로 액세스 토큰 발급
     */
    private String getAdminAccessToken() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("grant_type", "client_credentials");
        Map<String, Object> response = webClientBuilder.build()
                .post()
                .uri(keycloakBaseUrl + "/realms/" + realm + "/protocol/openid-connect/token")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        return (String) response.get("access_token");
    }
}
