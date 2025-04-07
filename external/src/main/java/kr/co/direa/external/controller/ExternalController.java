package kr.co.direa.external.controller;

import kr.co.direa.external.dto.PromptRequestDto;
import kr.co.direa.external.dto.SseMessageDto;
import kr.co.direa.external.dto.WeatherRequestDto;
import kr.co.direa.external.dto.WeatherResponseDto;
import kr.co.direa.external.service.GPTService;
import kr.co.direa.external.service.GitlabService;
import kr.co.direa.external.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/external")
@RequiredArgsConstructor
@Slf4j
public class ExternalController {
    private final GitlabService gitlabService;
    private final GPTService gptService;
    private final WeatherService weatherService;

    @GetMapping("/projects")
    public String getProjects() {
        return gitlabService.getProjects();
    }

    @PostMapping(value = "/ai", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<SseMessageDto>> generateStream(@RequestBody PromptRequestDto request) {
        return gptService.getStream(request.getMessage())
                .map(chatResponse -> {
                    String text = chatResponse.getResult().getOutput().getText();
                    return ServerSentEvent.builder(new SseMessageDto(text)).build();
                });
    }

    @GetMapping("/weather/{lat}/{lon}")
    public Mono<WeatherResponseDto> getWeather(
            @PathVariable String lat,
            @PathVariable String lon
    ) {
        WeatherRequestDto requestDto = new WeatherRequestDto(Double.parseDouble(lat), Double.parseDouble(lon));
        return weatherService.getWeatherData(requestDto);
    }
}
