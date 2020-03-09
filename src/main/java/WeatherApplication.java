import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

class WeatherApplication {

    private static final String appKey = "59fb36f901b9798c53b88d1d8bd0a3cd";


    public static void main(String[] args) {

        String[] listOfCities = {"Tel+Aviv", "Singapore", "Auckland", "Ushuaia", "Miami", "London", "Berlin", "Reykjavik", "Cape+Town", "Kathmandu"};
        CityWeatherDetails shortestDaylightCity;
        CityWeatherDetails longestDaylightCity;

        System.out.println("Weather Application Starts to scan  " + listOfCities.length + " Cities.....");

        // get the cities weather details
        ArrayList<CityWeatherDetails> citiesWeatherDetails = getCitiesWeather(listOfCities);

        // go over the cities and find the one with longest daylight and shortest daylight
        try {
            shortestDaylightCity = citiesWeatherDetails.stream().min(Comparator.comparing(CityWeatherDetails::getCityDaylightDuration)).orElseThrow((Supplier<Throwable>) NoSuchElementException::new);

            longestDaylightCity = citiesWeatherDetails.stream().max(Comparator.comparing(CityWeatherDetails::getCityDaylightDuration)).orElseThrow((Supplier<Throwable>) NoSuchElementException::new);
        } catch (Throwable throwable) {
            throw new RuntimeException("Error finding min/ max daylight on stream", throwable);
        }

        if (citiesWeatherDetails.size() != listOfCities.length) {
            System.out.println("There were error in reading some of the cities, the result refer only to " + citiesWeatherDetails.size() + "out of: " + listOfCities.length);
        }

        System.out.println("The temperature of the city with shortest daylight is: " + shortestDaylightCity.getTemperature() + " It is on: " + shortestDaylightCity.getCity() + " ,Daylight duration in Sec. is: " + shortestDaylightCity.getCityDaylightDuration());
        System.out.println("The temperature of the city with longest daylight is: " + longestDaylightCity.getTemperature() + " It is on: " + longestDaylightCity.getCity() + " ,Daylight duration in Sec. is: " + longestDaylightCity.getCityDaylightDuration());

    }

    private static ArrayList<CityWeatherDetails> getCitiesWeather(String[] listOfCities) {

        ArrayList<CityWeatherDetails> citySamples = new ArrayList<CityWeatherDetails>();
        for (String city : listOfCities) {
            CityWeatherDetails cityDetails = CityWeatherClient.getCityWeather(city, appKey);

            //adding only valid objects
            if (cityDetails.checkIfValid()) {
                citySamples.add(cityDetails);
            }


        }

        return citySamples;
    }


}