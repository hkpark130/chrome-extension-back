package kr.co.direa.external.service;

import kr.co.direa.external.dto.WeatherRequestDto;
import kr.co.direa.external.dto.WeatherResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WeatherServiceTest {

    @Autowired
    private WeatherService weatherService;

    @Test
    void getWeatherData() {
        // given
        WeatherRequestDto requestDto = new WeatherRequestDto(Double.parseDouble("37.5715388888888"),
                Double.parseDouble("126.961208333333"));
        Mono<WeatherResponseDto> weatherMono = weatherService.getWeatherData(requestDto);

        // when
        WeatherResponseDto result = weatherMono.block(); // 비동기 → 동기 변환

        // then
        assertThat(result).isNotNull();
        System.out.println("실제 날씨 결과:");
        System.out.println("온도: " + result.getTemperature());
        System.out.println("상태: " + result.getCondition());
        System.out.println("지역: " + result.getLocation());
        System.out.println("코드: " + result.getCode());
    }
}
