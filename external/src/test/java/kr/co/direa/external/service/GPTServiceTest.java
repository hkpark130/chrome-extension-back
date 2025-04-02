package kr.co.direa.external.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GPTServiceTest {

    private WebClient.Builder webClientBuilder;
    private WebClient webClient;
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;
    private WebClient.ResponseSpec responseSpec;

    private GPTService gptService;

    @BeforeEach
    void setUp() {
        // mock 생성
        webClientBuilder = mock(WebClient.Builder.class);
        webClient = mock(WebClient.class);
        requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);

        // WebClient 빌드 체인 스텁
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.header(eq(HttpHeaders.AUTHORIZATION), any(String.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.header(eq(HttpHeaders.CONTENT_TYPE), any(String.class))).thenReturn(requestBodyUriSpec);

        // ⚠️ 여기 핵심 포인트 - 컴파일 에러 해결을 위함
        when(requestBodyUriSpec.bodyValue(Mockito.any(Map.class))).thenReturn((WebClient.RequestHeadersSpec) requestHeadersSpec);

        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        gptService = new GPTService(webClientBuilder);
        gptService.setOpenAiApiKey("fake-api-key");
        gptService.setOpenAiEndpoint("https://api.openai.com/v1/chat/completions");
    }

    @Test
    void testAskReturnsExpectedResponse() {
        Map<String, Object> fakeOpenAIResponse = Map.of(
                "choices", List.of(
                        Map.of("message", Map.of("content", "Hello from GPT"))
                )
        );

        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(fakeOpenAIResponse));

        Mono<String> result = gptService.ask("안녕?");

        StepVerifier.create(result)
                .expectNext("Hello from GPT")
                .verifyComplete();
    }

    @Test
    void realGptCallTest() {
        Mono<String> result = gptService.ask("지금 몇 시야?");
        String response = result.block(); // 실제 네트워크 요청
        System.out.println("실제 GPT 응답 👉 " + response);
    }

    @Test
    void testAskHandlesErrorCorrectly() {
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.error(new RuntimeException("Outage")));

        Mono<String> result = gptService.ask("Break it");

        StepVerifier.create(result)
                .expectNextMatches(msg -> msg.contains("GPT 호출에 실패했습니다"))
                .verifyComplete();
    }
}