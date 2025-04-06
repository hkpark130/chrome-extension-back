package kr.co.direa.external.common;

public enum WeatherCondition {
    CLEAR("맑음", "meteocons-sun"),
    CLOUDY("흐림", "meteocons-cloud"),
    PARTLY_CLOUDY("구름많음", "meteocons-cloud-sun"),
    RAIN("비", "meteocons-rain"),
    SNOW("눈", "meteocons-snow"),
    SLEET("비/눈", "meteocons-sleet"),

    DRIZZLE("빗방울", "meteocons-drizzle"),
    SNOWFLAKE("눈날림", "meteocons-snowflake"),
    UNKNOWN("알 수 없음", "meteocons-cloud");

    private final String description;
    private final String iconClass;

    WeatherCondition(String description, String iconClass) {
        this.description = description;
        this.iconClass = iconClass;
    }

    public String getDescription() {
        return description;
    }

    public String getIconClass() {
        return iconClass;
    }
}
