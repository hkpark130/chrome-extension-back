package kr.co.direa.external.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Setter
@RequiredArgsConstructor
public class GPTService {

    @Value("${openai.api-key}")
    private String openAiApiKey;

    @Value("${openai.endpoint}")
    private String openAiEndpoint;

    @Qualifier("webClient")
    private final WebClient.Builder webClientBuilder;

    public Mono<String> ask(String userContent) {
        WebClient client = webClientBuilder.build();

        // 요청 Body 구성
        Map<String, Object> body = Map.of(
                "model", "gpt-4o",
                "max_tokens", 1024,
                "messages", List.of(
                        Map.of("role", "user", "content", userContent)
                )
        );

        return client.post()
                .uri(openAiEndpoint)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + openAiApiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    try {
                        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                        Map<String, Object> firstChoice = choices.get(0);
                        Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
                        log.info("💬 GPT 응답 본문: {}", response);
                        return (String) message.get("content");
                    } catch (Exception e) {
                        log.error("🟥 응답 파싱 에러: {}", e.getMessage());
                        return "GPT 응답을 이해하지 못했습니다.";
                    }
                })
                .onErrorResume(error -> {
                    log.error("🟥 GPT API 호출 실패: {}", error.getMessage());
                    return Mono.just("GPT 호출에 실패했습니다: " + error.getMessage());
                });
    }

}
