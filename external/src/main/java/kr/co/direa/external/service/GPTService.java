package kr.co.direa.external.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Setter
@RequiredArgsConstructor
public class GPTService {
    private final ChatModel chatModel; // OpenAiChatModel을 자동으로 주입받음

//    public ResponseEntity<SseEmitter> streamToSse(String message) {
//        SseEmitter emitter = new SseEmitter(0L); // 무제한 타임아웃 (원하면 30_000L 등 지정 가능)
//
//        Prompt prompt = new Prompt(message);
//        Flux<ChatResponse> responseFlux = chatModel.stream(prompt); // OpenAI API 호출 (stream=true)
//
//        responseFlux
//                .map(chatResponse -> chatResponse.getResult().getOutput().getContent())
//                .filter(content -> content != null && !content.isBlank())
//                .subscribe(
//                        content -> {
//                            try {
//                                emitter.send(SseEmitter.event()
//                                        .name("message")
//                                        .data(content));
//                            } catch (IOException e) {
//                                emitter.completeWithError(e);
//                            }
//                        },
//                        error -> emitter.completeWithError(error),
//                        emitter::complete
//                );
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_EVENT_STREAM_VALUE)
//                .body(emitter);
//    }

    public Flux<ChatResponse> getStream(String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return chatModel.stream(prompt);
    }
}
