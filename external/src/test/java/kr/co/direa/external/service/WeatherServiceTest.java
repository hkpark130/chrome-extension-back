package kr.co.direa.external.service;

import kr.co.direa.external.dto.WeatherDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WeatherServiceTest {

    @Autowired
    private WeatherService weatherService;

    @Test
    void getWeatherData() {
        // given
        Mono<WeatherDto> weatherMono = weatherService.getWeatherData();

        // when
        WeatherDto result = weatherMono.block(); // 비동기 → 동기 변환

        // then
        assertThat(result).isNotNull();
        System.out.println("🔥 실제 날씨 결과:");
        System.out.println("🌡️ 온도: " + result.getTemperature());
        System.out.println("🌥️ 상태: " + result.getCondition());
        System.out.println("📍 지역: " + result.getLocation());
        System.out.println("🎨 아이콘: " + result.getIconClass());
    }
}
