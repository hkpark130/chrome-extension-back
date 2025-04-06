package kr.co.direa.external.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.direa.external.common.WeatherCondition;
import kr.co.direa.external.dto.WeatherDto;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@Setter
@RequiredArgsConstructor
public class WeatherService {
    private final WebClient weatherWebClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String SERVICE_KEY = "인코딩키";
    private static final String BASE_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst";
    private static final String NX = "60"; // 강남
    private static final String NY = "127";

    private String buildUri(String baseDate, String baseTime) {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        urlBuilder.append("?serviceKey=").append(SERVICE_KEY);
        urlBuilder.append("&pageNo=1");
        urlBuilder.append("&numOfRows=100");
        urlBuilder.append("&dataType=JSON");
        urlBuilder.append("&base_date=").append(baseDate);
        urlBuilder.append("&base_time=").append(baseTime);
        urlBuilder.append("&nx=").append(NX);
        urlBuilder.append("&ny=").append(NY);

        String url = urlBuilder.toString();

        return url;
    }

    public Mono<WeatherDto> getWeatherData() {
        String baseDate = getBaseDate();
        String baseTime = getBaseTime();

        String uri = buildUri(baseDate, baseTime);

        log.info("날씨 API 요청 URI: {}", uri);

        return weatherWebClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::parseWeatherData);
    }

    private WeatherDto parseWeatherData(String json) {
        log.info("날씨 데이터 JSON: {}", json);
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode items = root.path("response").path("body").path("items").path("item");

            String nowFcstTime = getNowFcstTime(); // 현재 시각에 가장 가까운 fcstTime 계산

            String sky = null;
            String pty = null;
            String t1h = null;

            for (JsonNode item : items) {
                String category = item.path("category").asText();
                String fcstTime = item.path("fcstTime").asText();
                String value = item.path("fcstValue").asText();

                if (nowFcstTime.equals(fcstTime)) { // 현재 시간에 가장 가까운 fcstTime만
                    switch (category) {
                        case "SKY":
                            sky = value;
                            break;
                        case "PTY":
                            pty = value;
                            break;
                        case "T1H":
                            t1h = value;
                            break;
                    }
                }
            }

            // 값 없으면 기본값
            if (sky == null) sky = "1"; // 맑음
            if (pty == null) pty = "0"; // 비 없음
            if (t1h == null) t1h = "0"; // 0도

            WeatherCondition condition = mapCondition(sky, pty);

            return WeatherDto.builder()
                    .temperature(t1h)
                    .condition(condition.getDescription())
                    .location("강남구")
                    .iconClass(condition.getIconClass())
                    .build();

        } catch (Exception e) {
            log.error("날씨 데이터 파싱 중 오류 발생", e);
            // 실패 시 UNKNOWN 리턴
            return WeatherDto.builder()
                    .temperature("-")
                    .condition("정보없음")
                    .location("강남구")
                    .iconClass("meteocons-cloud")
                    .build();
        }
    }

    private String getNowFcstTime() {
        LocalTime now = LocalTime.now();
        int minute = now.getMinute();

        // 30분 전 기준 내림
        if (minute < 30) {
            now = now.minusHours(1);
        }

        return now.format(DateTimeFormatter.ofPattern("HH00"));
    }


    private WeatherCondition mapCondition(String sky, String pty) {
        if (!"0".equals(pty)) {
            switch (pty) {
                case "1": return WeatherCondition.RAIN;
                case "2", "6": return WeatherCondition.SLEET;
                case "3": return WeatherCondition.SNOW;
                case "5": return WeatherCondition.DRIZZLE;
                case "7": return WeatherCondition.SNOWFLAKE;
            }
        }

        switch (sky) {
            case "1": return WeatherCondition.CLEAR;
            case "3": return WeatherCondition.PARTLY_CLOUDY;
            case "4": return WeatherCondition.CLOUDY;
            default: return WeatherCondition.UNKNOWN;
        }
    }

    private String getBaseDate() {
        LocalDateTime now = LocalDateTime.now().minusMinutes(30);
        return now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    private String getBaseTime() {
        LocalTime now = LocalTime.now().minusMinutes(30);

        int hour = now.getHour();
        int minute = now.getMinute();

        // 30분 단위로 내림 처리
        if (minute < 30) {
            minute = 0;
        } else {
            minute = 30;
        }

        return String.format("%02d%02d", hour, minute);
    }
}
