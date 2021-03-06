import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Objects;


class CityWeatherClient {

    private static final String url = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";


    public static CityWeatherDetails getCityWeather(String city, String appkey) {

        if (city == null || city.isEmpty() || appkey == null || appkey.isEmpty()) {
            throw new RuntimeException("getCityWeather  params are null or empty");
        }
        String urlFormatted = String.format(url, city, appkey);
        JSONObject myRes = getCityWeather(urlFormatted);

        try {
            JSONObject sys = (JSONObject) Objects.requireNonNull(myRes).get("sys");
            long sunrise = (long) sys.get("sunrise");
            long sunset = (long) sys.get("sunset");
            JSONObject main = (JSONObject) myRes.get("main");
            double temperature = (double) main.get("temp");
            System.out.println("City: " + city + " temperature : " + temperature + " Daylight Duration in seconds : " + (sunset - sunrise));

            return new CityWeatherDetails(city, sunset, sunrise, temperature);

        } catch (Exception e) {
            throw new RuntimeException("There was a problem with one of the City Weather details: "+city+ " JSON is: "+myRes, e);
        }
    }


    private static JSONObject getCityWeather(String url) {

        HttpResponse response = null;
        final int numOfRetries = 5;
        int SLEEP_TIME = 5000;

        String entityText;

        CloseableHttpClient client = HttpClientBuilder.create().build();

        if (url == null || url.isEmpty()) {
            throw new RuntimeException("Url is null or empty");
        }
        HttpGet get = new HttpGet(url);

        int attempts = 0;
        try {
            while (response == null && attempts++ < numOfRetries) {
                try {

                    response = client.execute(get);
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode >= 400) {
                        throw new RuntimeException("Request: " + url + ", GET status code " + statusCode);

                    }
                    entityText = EntityUtils.toString(response.getEntity());
                    EntityUtils.consume(response.getEntity());

                    return (JSONObject) new JSONParser().parse(entityText);

                } catch (IOException e) {
                    System.out.println("Failed getting weather sample,  attempt " + attempts);
                    e.printStackTrace();
                    try {
                        Thread.sleep(SLEEP_TIME);
                    } catch (InterruptedException e1) {
                        System.out.println("Sleep interrupted error");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Could not parse response from website", e);
                }
            }
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}
