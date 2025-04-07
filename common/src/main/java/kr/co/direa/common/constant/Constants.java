package kr.co.direa.common.constant;

import org.springframework.context.annotation.Configuration;

@Configuration
public class Constants {
    public static final String GO_API_BASE_URL = "http://apis.data.go.kr";
    public static final String GO_API_WEATHER_URL = GO_API_BASE_URL + "/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst";
    public static final String GO_API_SERVICE_KEY = "vw";
    public static final String MAP_API_URL = "https://nominatim.openstreetmap.org";

}
