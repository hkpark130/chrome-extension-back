package kr.co.direa.external.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitlabService {
    @Qualifier("webClient")
    private final WebClient.Builder webClientBuilder;

    public String getProjects() {
        WebClient webClient = webClientBuilder
                .baseUrl("https://gitlab.com/api/v4")  // GitLab 공식 REST API endpoint
                .defaultHeader("PRIVATE-TOKEN", "your-access-token") // 🔐 GitLab personal token 넣기
                .build();

        String response = webClient.get()
                .uri("/projects") // ex: public projects 목록
                .retrieve()
                .bodyToMono(String.class)
                .block(); // reactive → 동기화

        return response;
    }
}
