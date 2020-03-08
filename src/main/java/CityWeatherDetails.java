public class CityWeatherDetails {

    private final String city;
    private final Long sunset;
    private final Long sunrise;
    private final Double temperature;

    public CityWeatherDetails(String city, Long sunset, Long sunrise, Double temperature) {
        this.city = city;
        this.sunset = sunset;
        this.sunrise = sunrise;
        this.temperature = temperature;

    }

    public Long getCityDaylightDuration() {
        return (this.sunset - this.sunrise);
    }

    public String getCity() {
        return this.city;
    }

    public Double getTemperature() {
        return this.temperature;
    }

    public Long getSunset() {
        return this.sunset;
    }

    public Long getSunrise() {
        return this.sunrise;
    }
}
