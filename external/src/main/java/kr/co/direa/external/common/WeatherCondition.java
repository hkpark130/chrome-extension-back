package kr.co.direa.external.common;

public enum WeatherCondition {
    CLEAR("맑음", "01d"),
    CLOUDY("흐림", "03d"),
    PARTLY_CLOUDY("구름많음", "02d"),
    RAIN("비", "09d"),
    SNOW("눈", "13d"),
    SLEET("비/눈", "13d"),
    DRIZZLE("빗방울", "10d"),
    SNOWFLAKE("눈날림", "13n"),
    UNKNOWN("알 수 없음", "01d");

    private final String description;
    private final String code;

    WeatherCondition(String description, String code) {
        this.description = description;
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }
}
