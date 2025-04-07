package kr.co.direa.external.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.direa.external.common.WeatherCondition;
import kr.co.direa.external.dto.WeatherRequestDto;
import kr.co.direa.external.dto.WeatherResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static kr.co.direa.common.constant.Constants.*;

@Slf4j
@Service
@Setter
@RequiredArgsConstructor
public class WeatherService {
    private final WebClient weatherWebClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String buildUri(String baseDate, String baseTime, String nx, String ny) {
        StringBuilder urlBuilder = new StringBuilder(GO_API_WEATHER_URL);
        urlBuilder.append("?serviceKey=").append(GO_API_SERVICE_KEY);
        urlBuilder.append("&pageNo=1");
        urlBuilder.append("&numOfRows=100");
        urlBuilder.append("&dataType=JSON");
        urlBuilder.append("&base_date=").append(baseDate);
        urlBuilder.append("&base_time=").append(baseTime);
        urlBuilder.append("&nx=").append(nx);
        urlBuilder.append("&ny=").append(ny);
        return urlBuilder.toString();
    }

    public Mono<WeatherResponseDto> getWeatherData(WeatherRequestDto requestDto) {
        String baseDate = getBaseDate();
        String baseTime = getBaseTime();
        Map<String, Integer> coordinate = convertLatLonToGrid(requestDto.getLat(), requestDto.getLon());

        String uri = buildUri(baseDate, baseTime, String.valueOf(coordinate.get("nx")), String.valueOf(coordinate.get("ny")));

        log.info("날씨 API 요청 URI: {}", uri);

        return weatherWebClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(json -> getAddressFromLatLon(requestDto.getLat(), requestDto.getLon())
                        .map(address -> parseWeatherData(json, address)));
    }

    private WeatherResponseDto parseWeatherData(String json, String address) {
        log.info("날씨 데이터 JSON: {}", json);

        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode items = root.path("response").path("body").path("items").path("item");

            String nowFcstTime = getNowFcstTime();

            String sky = null;
            String pty = null;
            String t1h = null;

            for (JsonNode item : items) {
                String category = item.path("category").asText();
                String fcstTime = item.path("fcstTime").asText();
                String value = item.path("fcstValue").asText();

                if (nowFcstTime.equals(fcstTime)) {
                    switch (category) {
                        case "SKY": sky = value; break;
                        case "PTY": pty = value; break;
                        case "T1H": t1h = value; break;
                    }
                }
            }

            if (sky == null) sky = "1";
            if (pty == null) pty = "0";
            if (t1h == null) t1h = "0";

            WeatherCondition condition = mapCondition(sky, pty);

            String iconCode = condition.getCode();
            if (iconCode.endsWith("d") || iconCode.endsWith("n")) {
                if (isNightTime()) {
                    iconCode = iconCode.replace("d", "n");
                } else {
                    iconCode = iconCode.replace("n", "d");
                }
            }

            return WeatherResponseDto.builder()
                    .temperature(t1h)
                    .condition(condition.getDescription())
                    .location(address)
                    .code(iconCode) // 대신 iconCode 사용
                    .build();

        } catch (Exception e) {
            log.error("날씨 데이터 파싱 중 오류 발생", e);
            return WeatherResponseDto.builder()
                    .temperature("-")
                    .condition("정보없음")
                    .location("위치 정보 없음")
                    .code("01d")
                    .build();
        }
    }

    private Mono<String> getAddressFromLatLon(double lat, double lon) {
        WebClient client = WebClient.builder()
                .baseUrl(MAP_API_URL)
                .defaultHeader("User-Agent", "MyWeatherApp/1.0")
                .build();

        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/reverse")
                        .queryParam("format", "json")
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        JsonNode json = objectMapper.readTree(response);
                        return json.path("display_name").asText("주소 정보 없음");
                    } catch (Exception e) {
                        log.error("주소 파싱 실패", e);
                        return "주소 정보 없음";
                    }
                })
                .onErrorReturn("주소 정보 없음");
    }

    private String getNowFcstTime() {
        LocalTime now = LocalTime.now().plusMinutes(30);
        int hour = now.getHour();

        return String.format("%02d00", hour);
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

    private boolean isNightTime() {
        int hour = LocalTime.now().getHour();
        return hour < 6 || hour >= 18;
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

    // 공공데이터 위경도 -> 격자좌표 변환 함수
    private Map<String, Integer> convertLatLonToGrid(double lat, double lon) {
        double RE = 6371.00877;
        double GRID = 5.0;
        double SLAT1 = 30.0;
        double SLAT2 = 60.0;
        double OLON = 126.0;
        double OLAT = 38.0;
        double XO = 210 / GRID;
        double YO = 675 / GRID;

        double DEGRAD = Math.PI / 180.0;
        double radLat = lat * DEGRAD;
        double radLon = lon * DEGRAD;
        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);

        double ra = Math.tan(Math.PI * 0.25 + radLat * 0.5);
        ra = re * sf / Math.pow(ra, sn);

        double theta = radLon - olon;
        if (theta > Math.PI) theta -= 2.0 * Math.PI;
        if (theta < -Math.PI) theta += 2.0 * Math.PI;
        theta *= sn;

        int nx = (int) (ra * Math.sin(theta) + XO + 0.5);
        int ny = (int) (ro - ra * Math.cos(theta) + YO + 0.5);

        return Map.of("nx", nx, "ny", ny);
    }
}
