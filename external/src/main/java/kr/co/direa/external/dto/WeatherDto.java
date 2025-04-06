package kr.co.direa.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDto {
    private String temperature; // 기온
    private String condition;   // 하늘 상태 (맑음, 흐림 등)
    private String location;    // 지역명 (예: 강남구)
    private String iconClass;   // meteocons 아이콘 클래스명
}
