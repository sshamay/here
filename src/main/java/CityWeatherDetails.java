public class CityWeatherDetails {

    private final String city;
    private final long sunset;
    private final long sunrise;
    private final double temperature;

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


    public boolean checkIfValid() {
        boolean isValid = false;
        if (this.temperature >= 0 && this.sunrise >= 0 && this.sunset >= 0 && getCityDaylightDuration() > 0) {

            isValid = true;
        }

        return isValid;
    }
}
